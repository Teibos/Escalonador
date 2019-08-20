package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;

public class FachadaEscalonador {
	

	private int quantum;
	private int tick;
	private TipoEscalonador tipoEscalonador;
	private ArrayList<String> listaProcesso;
	private String rodando;
	private ArrayList<String> processoBloqueado;
	private String execultando;
	private ArrayList<String> temp;



	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		this.quantum = 3;
		this.tick = 0;
		this.rodando = "";
		this.tipoEscalonador = tipoEscalonador;
		this.listaProcesso = new ArrayList<String>();
		this.processoBloqueado = new ArrayList<String>();
		this.temp = new ArrayList<String>();
				
		
	}

	public FachadaEscalonador(TipoEscalonador roundrobin, int quantum) {
	}

	public String getStatus() {

		
		if((this.listaProcesso.size() != 0) && (this.tick == 0)) {
			return ("Escalonador " + this.tipoEscalonador + ";"
					+ "Processos: {"+ this.rodando +"Fila: "+ this.listaProcesso.toString()+"};"
					+ "Quantum: " + this.quantum + ";"
					+ "Tick: " + this.tick);
			
		}else if ((this.listaProcesso.size() > 1) && (this.tick > 0)){
			return ("Escalonador " + this.tipoEscalonador + ";"
					+ "Processos: {"+ this.rodando +", Fila: "+"["+ this.listaProcesso.get(1) +"]"+"};"
					+ "Quantum: " + this.quantum + ";"
					+ "Tick: " + this.tick);
			
			
		}else {
			return ("Escalonador " + this.tipoEscalonador + ";"
					+ "Processos: {"+this.rodando+"};"
					+ "Quantum: " + this.quantum + ";"
					+ "Tick: " + this.tick);
		}
		
		//return null;
		
	}

	public void tick() {
		this.tick ++;
		if(this.listaProcesso.size() != 0) {
			this.rodando = "Rodando: "+this.listaProcesso.get(0);
			this.execultando = this.listaProcesso.get(0);
		}else {
			this.rodando = "";
		}if(this.listaProcesso.size() != 0) {
			if ((this.tick > this.quantum)&&(this.tick <= this.quantum*2)) {
				this.listaProcesso.remove(0);
				this.listaProcesso.add(execultando);
				this.rodando = "Rodando: "+this.listaProcesso.get(0);
				//this.temp = this.listaProcesso;
				//this.temp.remove(0);
				
			}else if((this.tick > this.quantum*2)&&(this.tick <= this.quantum*3)) {
				this.listaProcesso.remove(0);
				this.listaProcesso.add(execultando);
				this.rodando = "Rodando: "+this.listaProcesso.get(0);
				//this.temp = this.listaProcesso;
				//this.temp.remove(0);
				
			}
		}
		
		

	}

	public void adicionarProcesso(String nomeProcesso) {
		this.listaProcesso.add(nomeProcesso);
		
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
	}

	public void finalizarProcesso(String nomeProcesso) {
		
		this.listaProcesso.remove(0);
		//this.listaProcesso.remove(0);
			
	}

	public void bloquearProcesso(String nomeProcesso) {
	}

	public void retomarProcesso(String nomeProcesso) {
		
	}
}
