package escom.libreria.jdbc.reporteador.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CompraDTO implements Serializable{
	
	private Integer  idCompra,cantidad,No_Guia;
	private Date     fechaCompra;	
	private String nombre,paterno,materno, estado;
    private BigDecimal pago_neto,costo_envio;
    private String tipo_pago,descripcio,tipoEnvio,paqueteria,rfc;
    private Integer No_Serie,No_folio;
    private String fasctura,procedencia_almacen;
    private String idCliente;
    
    
    
   
   


	public CompraDTO() {
		// TODO Auto-generated constructor stub
	}






	public String getIdCliente() {
		return idCliente;
	}






	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}






	public Integer getIdCompra() {
		return idCompra;
	}






	public void setIdCompra(Integer idCompra) {
		this.idCompra = idCompra;
	}






	public Integer getCantidad() {
		return cantidad;
	}






	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}






	public Integer getNo_Guia() {
		return No_Guia;
	}






	public void setNo_Guia(Integer no_Guia) {
		No_Guia = no_Guia;
	}






	public Date getFechaCompra() {
		return fechaCompra;
	}






	public void setFechaCompra(Date fechaCompra) {
		this.fechaCompra = fechaCompra;
	}






	public String getNombre() {
		return nombre;
	}






	public void setNombre(String nombre) {
		this.nombre = nombre;
	}






	public String getPaterno() {
		return paterno;
	}






	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}






	public String getMaterno() {
		return materno;
	}






	public void setMaterno(String materno) {
		this.materno = materno;
	}






	public String getEstado() {
		return estado;
	}






	public void setEstado(String estado) {
		this.estado = estado;
	}






	public BigDecimal getPago_neto() {
		return pago_neto;
	}






	public void setPago_neto(BigDecimal pago_neto) {
		this.pago_neto = pago_neto;
	}






	public BigDecimal getCosto_envio() {
		return costo_envio;
	}






	public void setCosto_envio(BigDecimal costo_envio) {
		this.costo_envio = costo_envio;
	}






	public String getTipo_pago() {
		return tipo_pago;
	}






	public void setTipo_pago(String tipo_pago) {
		this.tipo_pago = tipo_pago;
	}






	public String getDescripcio() {
		return descripcio;
	}






	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}






	public String getTipoEnvio() {
		return tipoEnvio;
	}






	public void setTipoEnvio(String tipoEnvio) {
		this.tipoEnvio = tipoEnvio;
	}






	public String getPaqueteria() {
		return paqueteria;
	}






	public void setPaqueteria(String paqueteria) {
		this.paqueteria = paqueteria;
	}






	public String getRfc() {
		return rfc;
	}






	public void setRfc(String rfc) {
		this.rfc = rfc;
	}






	public Integer getNo_Serie() {
		return No_Serie;
	}






	public void setNo_Serie(Integer no_Serie) {
		No_Serie = no_Serie;
	}






	public Integer getNo_folio() {
		return No_folio;
	}






	public void setNo_folio(Integer no_folio) {
		No_folio = no_folio;
	}






	public String getFasctura() {
		return fasctura;
	}






	public void setFasctura(String fasctura) {
		this.fasctura = fasctura;
	}






	public String getProcedencia_almacen() {
		return procedencia_almacen;
	}






	public void setProcedencia_almacen(String procedencia_almacen) {
		this.procedencia_almacen = procedencia_almacen;
	}
	
	

}
