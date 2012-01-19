/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.generadorIDpromociones.ejb;

import escom.libreria.info.generadorIDpromociones.GeneradorContador;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author xxx
 */
@Stateless
public class GeneradorContadorFacade extends AbstractFacade<GeneradorContador> {
    @PersistenceContext(unitName = "LlaveCompuestaPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public GeneradorContadorFacade() {
        super(GeneradorContador.class);
    }

}
