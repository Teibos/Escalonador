package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;

public class FachadaEscalonador {
	
	private int quantum;
	private int tick;
	private TipoEscalonador tipoEscalonador;
	private ArrayList<String> listaProcesso;
	private String rodando;
	private ArrayList<String> processoBloqueado;
	

	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		this.quantum = 3;
		this.tick = 0;
		this.rodando = "";
		this.tipoEscalonador = tipoEscalonador;
		this.listaProcesso = new ArrayList<String>();
		this.processoBloqueado = new ArrayList<String>();
		
				
		
	}

	public FachadaEscalonador(TipoEscalonador roundrobin, int quantum) {
	}

	public String getStatus() {
		
		if(this.listaProcesso.size() == 0) {
			return ("Escalonador " + this.tipoEscalonador + ";"
					+ "Processos: {"+this.rodando+"};"
					+ "Quantum: " + this.quantum + ";"
					+ "Tick: " + this.tick);
		}
		else {
			return ("Escalonador " + this.tipoEscalonador + ";"
					+ "Processos: {Fila: "+ this.listaProcesso.toString()+"};"
					+ "Quantum: " + this.quantum + ";"
					+ "Tick: " + this.tick);
		}
		
		//return null;
		
	}

	public void tick() {
		this.tick ++;
		if(this.listaProcesso.size() != 0) {
			this.rodando = "Rodando: "+this.listaProcesso.get(0);
			this.listaProcesso.remove(0);
		}
		
	}

	public void adicionarProcesso(String nomeProcesso) {
		this.listaProcesso.add(nomeProcesso);
		
	}

	public void finalizarProcesso(String nomeProcesso) {
		boolean gato = true;
		
		for(String x: this.listaProcesso) {
			if(x == nomeProcesso) {
				gato = false;
				this.listaProcesso.remove(x);
			}
		}if(gato) {
			this.rodando = "Rodando: ";
		}
		
	}

	public void bloquearProcesso(String nomeProcesso) {
	}

	public void retomarProcesso(String nomeProcesso) {
		
	}
}
