package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FachadaEscalonador {

	private int quantum;
	private int tick;
	private TipoEscalonador tipoEscalonador;
	private Queue<String> listaProcesso;
	private String rodando;
	private Queue<String> processoBloqueado;
	// private Queue<Integer> tempTicks;
	private String aFinalizar;
	private String aBloquear;
	private int controle;
	private List<String> aRetomar;
	private List<Integer> tempDuracao;
	private List<Integer> prioridadesInteiros;
	private List<String> listaEMCP;
	private int tempRodandoMCP;

	private int gato;
	private int outroGato;

	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		this.quantum = 3;
		this.controle = 0;
		this.tick = 0;
		this.tipoEscalonador = tipoEscalonador;
		this.listaProcesso = new LinkedList<String>();
		this.processoBloqueado = new LinkedList<String>();
		// this.tempTicks = new LinkedList<Integer>();
		this.aRetomar = new ArrayList<String>();
		this.gato = 0;
		this.listaEMCP = new ArrayList<String>();
		this.prioridadesInteiros = new ArrayList<Integer>();
		if (tipoEscalonador == null) {
			throw new EscalonadorException();
		}
		if (tipoEscalonador.equals(tipoEscalonador.MaisCurtoPrimeiro)) {
			this.quantum = 0;
		}
		if (tipoEscalonador.equals(tipoEscalonador.Prioridade)) {
			this.prioridadesInteiros = new ArrayList<Integer>();
			this.tempDuracao = new ArrayList<Integer>();
		}

	}

	public FachadaEscalonador(TipoEscalonador roundrobin, int quantum) {
		this.quantum = quantum;
		this.controle = 0;
		this.tick = 0;
		this.tipoEscalonador = roundrobin;
		this.listaProcesso = new LinkedList<String>();
		this.processoBloqueado = new LinkedList<String>();
		// this.tempTicks = new LinkedList<Integer>();
		this.aRetomar = new ArrayList<String>();
		this.listaEMCP = new ArrayList<String>();
		this.prioridadesInteiros = new ArrayList<Integer>();
		this.gato = 0;
		if (quantum <= 0) {
			throw new EscalonadorException();
		}
		if (roundrobin == null) {
			throw new EscalonadorException();
		}
	}
	
	//Não apaga os comentarios, preciso deles para terminar o código

	public String getStatus() {

		String reslt = "";

		reslt += "Escalonador " + this.tipoEscalonador + ";";

		reslt += "Processos: {";

		if (rodando != null) {
			reslt += "Rodando: " + this.rodando;

		}
		if (listaProcesso.size() > 0) {
			if (rodando != null) {
				reslt += ", ";
			}
			reslt += "Fila: " + this.listaProcesso.toString();

		}
		// teste de mais curto primeiro listaEMCP
		if (listaEMCP.size() > 0) {
			if (rodando != null) {
				reslt += ", ";
			}
			reslt += "Fila: " + this.listaEMCP.toString();

		}
		if (processoBloqueado.size() > 0) {
			if (rodando != null) {
				reslt += ", ";
			}
			reslt += "Bloqueados: " + this.processoBloqueado.toString();

		}

		reslt += "};Quantum: " + this.quantum + ";";

		reslt += "Tick: " + this.tick;

		return reslt;
	}

	public void tick() {
		this.tick++;

		if (this.rodando == null) {
			if (this.listaProcesso.size() != 0) {
				this.rodando = this.listaProcesso.poll();
				this.controle = this.tick;
			}
		}
		if (aFinalizar != null) {
			if (this.rodando == this.aFinalizar) {
				this.rodando = null;
				if (this.listaProcesso.size() != 0) {
					this.rodando = this.listaProcesso.poll();
					this.controle = this.tick;
				}
			} else {
				this.listaProcesso.remove(aFinalizar);
			}
			this.aFinalizar = null;

		}
		if (aBloquear != null) {
			if (this.rodando == this.aBloquear) {
				this.rodando = null;
				this.processoBloqueado.add(aBloquear);
				if (this.listaProcesso.size() != 0) {
					this.rodando = this.listaProcesso.poll();
					this.controle = this.tick;
				}
			} else {
				this.listaProcesso.remove(aBloquear);
				this.processoBloqueado.add(aBloquear);

			}
			this.aBloquear = null;

		}
		if (this.aRetomar.size() > 0) {

			for (String k : this.aRetomar) {

				if (this.rodando == null) {
					this.rodando = k;
					this.controle = this.tick;
				} else {
					this.listaProcesso.add(k);

				}
				this.processoBloqueado.remove(k);
			}
			this.aRetomar.clear();

		}
		// Para trocar de processos
		if (this.listaProcesso.size() > 0) {
			if (this.rodando != null) {
				if (this.gato != 0) {
					this.controle = gato;
					this.gato = 0;
				}
				int temp = this.controle + this.quantum;
				if (temp == this.tick) {
					this.listaProcesso.add(this.rodando);
					this.rodando = this.listaProcesso.poll();
					this.controle = this.tick;
				}
			}
		}

		// Parte de Prioridade
		/**
		 * if (this.tempDuracao != null) { for ( int t : this.prioridadesInteiros) {
		 * 
		 * } }
		 * 
		 * a ideia aqui era mudar a lógica do número das prioridades, mas teria que mudar
		 * a primeira com o acesso a parte3 para poder aceitar o 2
		 * tu fica com essa parte
		 **/

		// Parte do mais Curto Primeiro

		if (this.tipoEscalonador.equals(tipoEscalonador.MaisCurtoPrimeiro)) {
			if (this.listaEMCP.size() > 0) {
				if (this.rodando == null) {
					this.rodando = this.listaEMCP.remove(0);
					this.tempRodandoMCP = this.prioridadesInteiros.remove(0);
					this.outroGato = this.tick + this.tempRodandoMCP;
				}
			}if (this.outroGato == this.tick && this.rodando != null) {
				if (this.listaEMCP.size() > 0) {
					this.rodando = this.listaEMCP.remove(0);
					this.tempRodandoMCP = this.prioridadesInteiros.remove(0);
				} else {
					this.rodando = null;
					this.tempRodandoMCP = 0;
				}
				if (this.tempRodandoMCP > 0) {
					this.outroGato = this.tick + this.tempRodandoMCP;
				}
			}
		}
	}

	public void adicionarProcesso(String nomeProcesso) {
		if (this.listaProcesso.contains(nomeProcesso) || this.rodando == nomeProcesso) {
			throw new EscalonadorException();
		} else if (tipoEscalonador.equals(tipoEscalonador.Prioridade)) {
			throw new EscalonadorException();
		} else {
			this.listaProcesso.add(nomeProcesso);
			if (this.tick != 0) {
				this.gato = this.tick + 1;
			}
		}
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
		if (tipoEscalonador.equals(escalonadorRoundRobin())) {
			throw new EscalonadorException();
		} else if (this.listaProcesso.contains(nomeProcesso) || this.rodando == nomeProcesso) {
			throw new EscalonadorException();
		} else if (prioridade > 4) {
			throw new EscalonadorException();
		} else {
			this.listaProcesso.add(nomeProcesso);
			/**
			 * if (this.tick != 0) { this.gato = this.tick + 1; }
			 */
		}
	}

	public void finalizarProcesso(String nomeProcesso) {
		if (this.rodando == nomeProcesso || this.listaProcesso.contains(nomeProcesso)) {
			this.aFinalizar = nomeProcesso;
		} else {
			throw new EscalonadorException();
		}

	}

	public void bloquearProcesso(String nomeProcesso) {
		if (this.rodando != nomeProcesso) {
			throw new EscalonadorException();
		} else if (this.rodando == nomeProcesso) {
			this.aBloquear = nomeProcesso;
		} else {
			throw new EscalonadorException();
		}

	}

	public void retomarProcesso(String nomeProcesso) {
		// this.aRetomar.add(nomeProcesso);
		/*
		 * if (this.aRetomar.contains(nomeProcesso)) { throw new EscalonadorException();
		 * }else { this.aRetomar.add(nomeProcesso);
		 * 
		 * }
		 */
		if (this.processoBloqueado.contains(nomeProcesso)) {
			this.aRetomar.add(nomeProcesso);
		} else {
			throw new EscalonadorException();
		}

	}

	public TipoEscalonador escalonadorRoundRobin() {
		return TipoEscalonador.RoundRobin;
	}

	public void adicionarProcessoTempoFixo(String string, int duracao) {
		// this.listaProcesso.add(string);
		// this.tempDuracao.add(duracao);
		/*
		 * if (this.listaEMCP.contains(string) || string == null) { throw new
		 * EscalonadorException();
		 * 
		 * }
		 * 
		 * if (duracao < 1) { throw new EscalonadorException(); }
		 */

		int maisCurto = Integer.MAX_VALUE;

		if (this.listaEMCP.size() == 0) {
			this.listaEMCP.add(string);
			this.prioridadesInteiros.add(duracao);
		} else {
			int menorPosicao = 0;
			this.listaEMCP.add(string);
			this.prioridadesInteiros.add(duracao);
			for (int i = 0; i < this.prioridadesInteiros.size(); i++) {
				if (this.prioridadesInteiros.get(i) < maisCurto) {
					maisCurto = this.prioridadesInteiros.get(i);
					menorPosicao = i;
				}
			}
			if (menorPosicao > 0) {
				String processoMenor = this.listaEMCP.remove(menorPosicao);
				int processoMenorTempo = this.prioridadesInteiros.remove(menorPosicao);
				this.listaEMCP.add(0, processoMenor);
				this.prioridadesInteiros.add(0, processoMenorTempo);
				for (int k = 0; duracao < k; k++) {
					this.listaEMCP.add(0, string);
					this.prioridadesInteiros.add(0, duracao);
				}
			}
		}

	}
}
