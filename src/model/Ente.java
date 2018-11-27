package model;

public class Ente {
	private int id;
	private String nome;
	private String url;
	
	public Ente() {
		this.id = 0;
		this.nome = "";
		this.url = "";
	}

	public Ente(int id, String nome, String url) {
		super();
		this.id = id;
		this.nome = nome;
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Ente)) {
			return false;
		}
		Ente other = (Ente) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s", nome);
	}
	
	

}
