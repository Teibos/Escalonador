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
	//private Queue<Integer> tempTicks;
	private String aFinalizar;
	private String aBloquear;
	private int controle;
	private List<String> aRetomar;
	
	
	private int gato;



	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		this.quantum = 3;
		this.controle = 0;
		this.tick = 0;
		this.tipoEscalonador = tipoEscalonador;
		this.listaProcesso = new LinkedList<String>();
		this.processoBloqueado = new LinkedList<String>();
		//this.tempTicks = new LinkedList<Integer>();
		this.aRetomar = new ArrayList<String>();
		this.gato = 0;
		if(tipoEscalonador == null) {
			throw new EscalonadorException();
		}
		
	}

	public FachadaEscalonador(TipoEscalonador roundrobin, int quantum) {
		this.quantum = quantum;
		this.controle = 0;
		this.tick = 0;
		this.tipoEscalonador = roundrobin;
		this.listaProcesso = new LinkedList<String>();
		this.processoBloqueado = new LinkedList<String>();
		//this.tempTicks = new LinkedList<Integer>();
		this.aRetomar = new ArrayList<String>();
		this.gato = 0;
		if(quantum <= 0) {
			throw new EscalonadorException();
		}if(roundrobin == null) {
			throw new EscalonadorException();
		}
	}

	public String getStatus() {
	
		String reslt = "";
		
		reslt += "Escalonador " + this.tipoEscalonador + ";";
		
		reslt += "Processos: {";
		
		if (rodando != null) {
			reslt += "Rodando: "+ this.rodando;
			
		}if (listaProcesso.size()>0) {
			if (rodando != null) {
				reslt += ", ";
			}reslt += "Fila: "+ this.listaProcesso.toString();
			
		}if (processoBloqueado.size()>0) {
			if (rodando != null) {
				reslt += ", ";
			}reslt += "Bloqueados: "+ this.processoBloqueado.toString();
			
		}
		
		
		reslt += "};Quantum: " + this.quantum + ";";
		
		reslt += "Tick: " + this.tick;
		
		return reslt;
	}

	public void tick() {
		this.tick ++;
		
		if(this.rodando == null) {
			if(this.listaProcesso.size() != 0) {
				this.rodando = this.listaProcesso.poll();
				this.controle = this.tick;
			}
		}
		if (aFinalizar != null) {
			if(this.rodando == this.aFinalizar) {
				this.rodando = null;
				if(this.listaProcesso.size() != 0) {
					this.rodando = this.listaProcesso.poll();
					this.controle = this.tick;
				}
			}else {
				this.listaProcesso.remove(aFinalizar);
			}
			this.aFinalizar = null;	
			
		}if (aBloquear != null) {
			if(this.rodando == this.aBloquear) {
				this.rodando = null;
				this.processoBloqueado.add(aBloquear);
				if(this.listaProcesso.size() != 0) {
					this.rodando = this.listaProcesso.poll();
					this.controle = this.tick;
				}
			}else {
				this.listaProcesso.remove(aBloquear);
				this.processoBloqueado.add(aBloquear);
			}
			this.aBloquear = null;
			
		}if (this.aRetomar.size()>0) {
			for(String k: this.aRetomar) {
				this.listaProcesso.add(k);
				this.processoBloqueado.remove(k);
			}
			this.aRetomar.clear();
			
			
			if(this.rodando == null) {
				this.rodando = this.listaProcesso.poll();
			}
		}
		
		//Para trocar de processos
		if(this.listaProcesso.size() > 0) {
			if(this.rodando != null) {
				if(this.gato != 0) {
					this.controle = gato;
					this.gato = 0;
				}
				int temp = this.controle + this.quantum;
				if(temp == this.tick) {
					this.listaProcesso.add(this.rodando);
					this.rodando = this.listaProcesso.poll();
					this.controle = this.tick;
					
				}
			}
			
		}
		
		
	}

	public void adicionarProcesso(String nomeProcesso) {
		if(this.listaProcesso.contains(nomeProcesso) || this.rodando == nomeProcesso) {
			throw new EscalonadorException();
		}else {
			this.listaProcesso.add(nomeProcesso);
			if(this.tick != 0) {
				this.gato = this.tick + 1;
			}
		}
		
		
		
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
		
		if(tipoEscalonador.equals(escalonadorRoundRobin())) {
			throw new EscalonadorException();
		}else {
			this.listaProcesso.add(nomeProcesso);
			if(this.tick != 0) {
				this.gato = this.tick + 1;
			}
		}
	}

	public void finalizarProcesso(String nomeProcesso) {
		if(this.rodando == nomeProcesso || this.listaProcesso.contains(nomeProcesso)) {
			this.aFinalizar = nomeProcesso;
		}else {
			throw new EscalonadorException();
		}
		
			
	}

	public void bloquearProcesso(String nomeProcesso) {
		if(this.rodando != nomeProcesso) {
			throw new EscalonadorException();
		}else if(this.rodando == nomeProcesso) {
			this.aBloquear = nomeProcesso;
		}else {
			throw new EscalonadorException();
		}
		
	}

	public void retomarProcesso(String nomeProcesso) {
		//this.aRetomar.add(nomeProcesso);
		/*
		if (this.aRetomar.contains(nomeProcesso)) {
			throw new EscalonadorException();
		}else {
			this.aRetomar.add(nomeProcesso);
			
		}*/
		if(this.processoBloqueado.contains(nomeProcesso)) {
			this.aRetomar.add(nomeProcesso);
		}else {
			throw new EscalonadorException();
		}
		
	}
	public TipoEscalonador escalonadorRoundRobin() {
		return TipoEscalonador.RoundRobin;
	}
}
