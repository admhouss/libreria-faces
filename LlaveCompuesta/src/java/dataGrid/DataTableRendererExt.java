/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dataGrid;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.DataTableRenderer;

/**
 *
 * @author xxx
 */
public class DataTableRendererExt extends DataTableRenderer
{

    @Override
    protected void encodeMarkup(FacesContext context, DataTable table) throws IOException{
        ResponseWriter writer = context.getResponseWriter();
        String clientId = table.getClientId(context);
        boolean scrollable = table.isScrollable();
        String containerClass = scrollable ? DataTable.CONTAINER_CLASS + " " + DataTable.SCROLLABLE_CONTAINER_CLASS : DataTable.CONTAINER_CLASS;
        containerClass = table.getStyleClass() != null ? containerClass + " " + table.getStyleClass() : containerClass;
        String style = null;
        boolean hasPaginator = table.isPaginator();
        String paginatorPosition = table.getPaginatorPosition();

        if(hasPaginator) {
            table.resetPagination();
            table.calculatePage();
        }

        writer.startElement("div", table);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", containerClass, "styleClass");
        if((style = table.getStyle()) != null) {
            writer.writeAttribute("style", style, "style");
        }

        encodeFacet(context, table, table.getHeader(), DataTable.HEADER_CLASS);

        if(hasPaginator && !paginatorPosition.equalsIgnoreCase("bottom")) {
            encodePaginatorMarkup(context, table, "top");
        }

        if(scrollable) {
            encodeScrollableTable(context, table);

        } else {
            encodeRegularTable(context, table);
        }

        if(hasPaginator && !paginatorPosition.equalsIgnoreCase("top")) {
            encodePaginatorMarkup(context, table, "bottom");
        }

        encodeFacet(context, table, table.getFooter(), DataTable.FOOTER_CLASS);

        if(table.isSelectionEnabled()) {
            encodeSelectionHolder(context, table);
        }

        writer.endElement("div");
    }

    /**
     * @see org.primefaces.component.datatable.DataTableRenderer#encodeTbody(javax.faces.context.FacesContext, org.primefaces.component.datatable.DataTable)
     * Fix for lazy load bug: data table is empty on very first load because of wrong palcement of table.loadLazyData();
     */
    @Override
    protected void encodeTbody(FacesContext context, DataTable table) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String rowIndexVar = table.getRowIndexVar();
        String clientId = table.getClientId(context);
        String emptyMessage = table.getEmptyMessage();
        String selectionMode = table.getSelectionMode();
        String columnSelectionMode = table.getColumnSelectionMode();
        String selMode = selectionMode != null ? selectionMode : columnSelectionMode != null ? columnSelectionMode : null;
        Object selection = table.getSelection();


        if(table.isLazy()) {
            table.loadLazyData();
        }

        int rows = table.getRows();
        int first = table.getFirst();
        int rowCount = table.getRowCount();
        int rowCountToRender = rows == 0 ? rowCount : rows;
        boolean hasData = rowCount > 0;



        String tbodyClass = hasData ? DataTable.DATA_CLASS : DataTable.EMPTY_DATA_CLASS;

        writer.startElement("tbody", null);
        writer.writeAttribute("id", clientId + "_data", null);
        writer.writeAttribute("class", tbodyClass, null);

        if(hasData) {
            if(selectionMode != null && selection != null) {
                handlePreselection(table, selectionMode, selection);
            }

            for(int i = first; i < (first + rowCountToRender); i++) {
                encodeRow(context, table, clientId, i, rowIndexVar);
            }
        }
        else if(emptyMessage != null){
            //Empty message
            writer.startElement("tr", null);
            writer.writeAttribute("class", DataTable.ROW_CLASS, null);

            writer.startElement("td", null);
            writer.writeAttribute("colspan", table.getColumns().size(), null);
            writer.write("&nbsp;");
            writer.endElement("td");

            writer.endElement("tr");
        }

        writer.endElement("tbody");

        //Cleanup
        table.setRowIndex(-1);
        if(rowIndexVar != null) {
            context.getExternalContext().getRequestMap().remove(rowIndexVar);
        }
    }

    private void handlePreselection(DataTable table, String selectionMode, Object selection) {
                System.out.println("tabla"+table+"String"+selectionMode+"Object"+selection);
    }

    private void encodeRow(FacesContext context, DataTable table, String clientId, int i, String rowIndexVar) {

    }
}
