/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.generarFactura.jsf;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.bind.v2.WellKnownNamespace;

/**
 *
 * @author xxx
 */
class MyNamespacePrefixMapper extends NamespacePrefixMapper{

    public MyNamespacePrefixMapper() {
    }

   

    @Override
    public String[] getPreDeclaredNamespaceUris() {
         return new String[]{WellKnownNamespace.XML_SCHEMA_INSTANCE};
    }


    @Override
    public String getPreferredPrefix(String string, String string1, boolean bln) {
         if (string.equals(WellKnownNamespace.XML_SCHEMA_INSTANCE))
            return "xsi";

       return "fx";
    }
/*
   @Override
    public String[] getContextualNamespaceDecls() {
        //return super.getContextualNamespaceDecls();
       return new String[]{"xsi"};
    }*/

   /*
    @Override
    public String[] getPreDeclaredNamespaceUris2() {
        return new String[]{"xsi"};
    }
    */



}

