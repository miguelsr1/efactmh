package sv.com.jsoft.efactmh.db.util;

/**
 *
 * @author msanchez
 */
public class NativeQuery {

    public final static String FIND_CLIENT_AUTOCOMPLETE = "select "
            + "                 case cli.tipo_personeria\n"
            + "                     when 'J' then emp.id_cliente\n"
            + "                     else per.id_cliente \n"
            + "			end as \"idCliente\",\n"
            + "            	case cli.tipo_personeria\n"
            + "            		when 'J' then emp.razon_social\n"
            + "            		else null\n"
            + "            	end as \"razonSocial\",\n"
            + "            	case cli.tipo_personeria\n"
            + "            		when 'J' then emp.nombre_completo\n"
            + "            		else per.nombre_completo\n"
            + "            	end as \"nombreCompleto\",\n"
            + "            	cli.tipo_personeria as \"tipoPersoneria\",\n"
            + "            	case cli.tipo_personeria\n"
            + "            		when 'J' then true\n"
            + "            		else per.inscrito_iva\n"
            + "            	end as \"inscritoIva\"\n"
            + "            from contribuyente con\n"
            + "            	inner join cliente cli on con.id_contribuyente = cli.id_contribuyente\n"
            + "                left join persona_natural per on cli.id_cliente = per.id_cliente\n"
            + "                left join empresa emp on cli.id_cliente = emp.id_cliente\n"
            + "            where ((per.dui like :query or\n"
            + "            		per.nombre_completo ilike :query) or\n"
            + "            	   (emp.nit_empresa like :query or\n"
            + "            	   emp.razon_social ilike :query or\n"
            + "            	   emp.nombre_comercial ilike :query)) and\n"
            + "            	con.correo = :correo";

    public final static String FIND_CLIENT = "select "
            + "                 case cli.tipo_personeria\n" 
            + "                     when 'J' then emp.id_cliente\n" 
            + "                     else per.id_cliente \n" 
            + "			end as \"idCliente\",\n"
            + "            	case cli.tipo_personeria\n"
            + "            		when 'J' then emp.razon_social\n"
            + "            		else null\n"
            + "            	end as \"razon_social\",\n"
            + "            	case cli.tipo_personeria\n"
            + "            		when 'J' then emp.nombre_completo\n"
            + "            		else per.nombre_completo\n"
            + "            	end as \"nombre_completo\",\n"
            + "            	cli.tipo_personeria,\n"
            + "            	case cli.tipo_personeria\n"
            + "            		when 'J' then true\n"
            + "            		else per.inscrito_iva\n"
            + "            	end as \"inscrito_iva\"\n"
            + "            from contribuyente con\n"
            + "            	inner join cliente cli on con.id_contribuyente = cli.id_contribuyente\n"
            + "                left join persona_natural per on cli.id_cliente = per.id_cliente\n"
            + "                left join empresa emp on cli.id_cliente = emp.id_cliente\n"
            + "            where cli.id_cliente = ?";
}
