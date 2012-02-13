package escom.libreria.jdbc.reporteador.dto;

public class SuscripcionDTO {
	
	private String suscripcion;
	private String articulo;
	
	private String descripcion;
	private Integer numero;	
	private String nombreCliente;	
	
	private Integer estado_envio;	
	private java.util.Date fecha_envio;
	public String getSuscripcion() {
		return suscripcion;
	}
	public void setSuscripcion(String suscripcion) {
		this.suscripcion = suscripcion;
	}
	public String getArticulo() {
		return articulo;
	}
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public Integer getEstado_envio() {
		return estado_envio;
	}
	public void setEstado_envio(Integer estado_envio) {
		this.estado_envio = estado_envio;
	}
	public java.util.Date getFecha_envio() {
		return fecha_envio;
	}
	public void setFecha_envio(java.util.Date fecha_envio) {
		this.fecha_envio = fecha_envio;
	}
	
	


}
