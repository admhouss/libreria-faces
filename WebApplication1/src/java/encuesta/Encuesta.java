/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package encuesta;

/**
 *
 * @author Joshelyne
 */
public class Encuesta{
String name;
String sexo;
String moto;
String automovil;

public Encuesta(){
}

public Encuesta(String name,String sexo,String moto, String automovil)
{
this.name=name;
this.sexo=sexo;
this.moto=moto;
this.automovil=automovil;
}


public String getName(){
	return name;
}

public void setName(String name){
this.name=name;
}

public String getSexo(){
	return sexo;
}

public void setSexo(String sexo){
this.sexo=sexo;
}

public String getMoto(){
	return moto;
}

public void setMoto(String moto){
this.moto=moto;
}

public String getAutomovil(){
	return automovil;
}

public void setAutomovil(String automovil){
this.automovil=automovil;
}

public String toString(){
return name+ " genero: "+sexo+" moto: "+moto+" carro: "+automovil;

}
}
