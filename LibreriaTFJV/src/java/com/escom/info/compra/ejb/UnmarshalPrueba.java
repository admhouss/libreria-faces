/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.compra.ejb;

import escom.libreria.cdi.Comprobante;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author xxx
 */
public class UnmarshalPrueba {

    private Unmarshaller unmarshaller;
    public Comprobante cargaArchivoComprobante(String rutaCompletaXML) throws FileNotFoundException {


            Class[] classes = new Class[10];
            classes[0] = escom.libreria.cdi.Comprobante.class;
            classes[1] = escom.libreria.cdi.Comprobante.Conceptos.class;
            classes[2] = escom.libreria.cdi.Comprobante.Emisor.class;
            classes[3] = escom.libreria.cdi.Comprobante.Impuestos.class;
            classes[4] = escom.libreria.cdi.Comprobante.Receptor.class;
            classes[5] = escom.libreria.cdi.TUbicacion.class;
            classes[6] = escom.libreria.cdi.TUbicacionFiscal.class;
            classes[7] = escom.libreria.cdi.Comprobante.Conceptos.Concepto.class;
            classes[8] = escom.libreria.cdi.Comprobante.Impuestos.Traslados.class;
            classes[9] = escom.libreria.cdi.Comprobante.Impuestos.Traslados.Traslado.class;

        FileInputStream fileInputStream = null;
        JAXBElement o = null;
        Comprobante comprobt3 = null;
        try {
            fileInputStream = new FileInputStream(rutaCompletaXML);
//            o = (JAXBElement) unmarshaller.unmarshal(fileInputStream);
                JAXBContext jaxbContext = JAXBContext.newInstance(classes);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                unmarshaller = jaxbContext.createUnmarshaller();
                comprobt3 = (Comprobante) unmarshaller.unmarshal(fileInputStream);

        } catch (JAXBException ex) {
            System.out.println("error de cargaArchivoFiscal JAXB -->" + ex);
        } catch (FileNotFoundException ex) {
            System.out.println("error de cargaArchivoFiscal I/O -->" + ex);
        }finally{
          try {
                fileInputStream.close();
            } catch (IOException ex) {
               System.out.println("error al cerrar buffer");
            }
        }
        return comprobt3;
    }

}
