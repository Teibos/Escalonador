package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FachadaEscalonador {
	

	private int quantum;
	private int tick;
	private TipoEscalonador tipoEscalonador;
	private Queue<String> listaProcesso;
	private String rodando;
	private ArrayList<String> processoBloqueado;
	private ArrayList<String> temp;
	private String aFinalizar;
	private int gato;



	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		this.quantum = 3;
		this.tick = 0;
		//this.rodando = "";
		this.tipoEscalonador = tipoEscalonador;
		this.listaProcesso = new LinkedList<String>();
		this.processoBloqueado = new ArrayList<String>();
		this.temp = new ArrayList<String>();
				
		
	}

	public FachadaEscalonador(TipoEscalonador roundrobin, int quantum) {
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
			
		}reslt += "};Quantum: " + this.quantum + ";";
		
		reslt += "Tick: " + this.tick;
		
		return reslt;
	}

	public void tick() {
		this.tick ++;
		
		if(this.rodando == null) {
			if(this.listaProcesso.size() != 0) {
				this.rodando = this.listaProcesso.poll();
			}
		}
		if (aFinalizar != null) {
			if(this.rodando == this.aFinalizar) {
				this.rodando = null;
			}else {
				this.listaProcesso.remove(aFinalizar);
			}
		}if((this.gato + this.quantum) == this.tick ) {
			this.listaProcesso.add(rodando);
			this.rodando = this.listaProcesso.poll();
		}
		
	}

	public void adicionarProcesso(String nomeProcesso) {
		this.listaProcesso.add(nomeProcesso);
		this.gato = this.tick;//fazer isso na lista
		
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
	}

	public void finalizarProcesso(String nomeProcesso) {
		this.aFinalizar = nomeProcesso;
			
	}

	public void bloquearProcesso(String nomeProcesso) {
	}

	public void retomarProcesso(String nomeProcesso) {
		
	}
}
