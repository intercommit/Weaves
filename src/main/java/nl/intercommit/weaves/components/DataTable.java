/*  Copyright 2014 InterCommIT b.v.
*
*  This file is part of the "Weaves" project hosted on https://github.com/intercommit/Weaves
*
*  Weaves is free software: you can redistribute it and/or modify
*  it under the terms of the GNU Lesser General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  any later version.
*
*  Weaves is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with Weaves.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package nl.intercommit.weaves.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
/**
 * A jquery DataTable : http://www.datatables.net
 * 
 * Only client side rendering is supported, serverside needs to be implemented.
 * 
 * @tapestrydoc
 */
@Import(library={"datatable/datatable.js","../jquery/dataTables.min.js"},stylesheet="datatable/datatable.css",stack="jquery")
@SupportsInformalParameters
public class DataTable extends nl.intercommit.weaves.base.BasicClientElement {
	
	@Parameter(allowNull=false,required=true,defaultPrefix=BindingConstants.PROP)
	private GridDataSource source;
	
	@Parameter(value = "this", allowNull = false)
    @Property(write = false)
    private PropertyOverrides overrides;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String include;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String add;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String exclude;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String reorder;
	
	@Parameter(allowNull=true)
	private JSONObject parameters;
	
	@Parameter(allowNull=true,defaultPrefix=BindingConstants.LITERAL)
	private String sortColumn;
	
	@Parameter(value="literal:asc",defaultPrefix=BindingConstants.LITERAL,allowNull=false)
	private String sortDirection;
	
	@Property
	@Parameter
	private int rowIndex;
	
	@Property
	@Parameter()
	private Object row;
	
	@Inject
	private BeanModelSource bms;
	
	@Inject
    private JavaScriptSupport scriptSupport;
	
	@Inject
	private Messages msgs;
	
	@Property
	@Parameter(allowNull=true)
	private BeanModel<?> model;
	
	private String header;
	
	@Property
	private String propertyName;
	
	@Inject
	private Block empty;
	
	@Inject
	@Symbol(nl.intercommit.weaves.SymbolConstants.BOOTSTRAP_ENABLED)
	private boolean bootstrap;
	
	private boolean renderBody;
	
	Object setupRender() {
		if (model == null) {
			final Class<?> rowType = source.getRowType();

            if (rowType == null)
                throw new RuntimeException(
                        String.format(
                                "Unable to determine the bean type for rows from %s. You should bind the model parameter explicitly.",
                                source));
			
			model = bms.createDisplayModel(rowType,msgs);
		}
		return source.getAvailableRows() == 0 ? empty : null;
    }
	
	@BeginRender
	private boolean setupDataTable() {
		renderBody = false;
		if (source.getAvailableRows() == 0) return renderBody;
		
		renderBody = true;
		
		BeanModelUtils.modify(model, add, include, exclude, reorder);
		
		if (parameters == null) {
			parameters = new JSONObject();
			parameters.put("bSort", true);
			parameters.put("bFilter", false);
			if (source.getAvailableRows() < 10) {
				parameters.put("bPaginate", false);
				parameters.put("sDom","<'top'i>");
			} else {
				parameters.put("sDom","<'top'lp>");
			}
			if (sortColumn != null) {
				// add some sorting
				int position = 0;
				for (String prop : model.getPropertyNames()) {
					if (prop.equalsIgnoreCase(sortColumn)) {
						break;
					}
					position++;
				}
				JSONArray sort = new JSONArray();
				sort.put(position);
				sort.put(sortDirection);
				
				parameters.put("aaSorting", new JSONArray(sort));
			}
			
		}
		return renderBody;
	}
	
	
	@AfterRender
    private void afterRender(MarkupWriter writer) {
		if (renderBody) {
			scriptSupport.addScript(InitializationPriority.LATE,"initializeTable('#"+getClientId()+"',"+parameters.toCompactString()+");");
		}
	}
	
	public List<Object> getRows() {
		List<Object> rows = new ArrayList<Object>();
		for (int i=0;i<source.getAvailableRows();i++) {
			rows.add(source.getRowValue(i));
		}
		return rows;
	}
	
	public PropertyModel getPropModel() {
		return model.get(propertyName);
	}
	
	public String getHeader() {
		if (msgs.contains(header+"-label")) {
			return msgs.get(header+"-label");
		}
		return header;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public String getTableClass() {
		if (bootstrap) {
			return "table";
		} else {
			return "display";
		}
	}
}
