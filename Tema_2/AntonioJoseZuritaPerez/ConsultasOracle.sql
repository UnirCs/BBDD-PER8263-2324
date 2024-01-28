-- Mostrar el nombre y apellido de un empleado junto con el nombre de su departamento.

/*

<?xml version="1.0" encoding="UTF-8"?>
<xs:schema
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:xdb="http://xmlns.oracle.com/xdb">
	<xs:element name="empleados">
		<xs:complexType>
			<xs:attribute name="nombre" type="xs:string" />
			<xs:attribute name="apellidos" type="xs:string" />
			<xs:attribute name="departamento" type="xs:string"/>
		</xs:complexType>
	</xs:element>
</xs:schema> 

*/

SELECT XMLELEMENT("empleados",
    XMLATTIRIBUTES(e.first_name AS ".nombre", -- XMLATTIRIBUTES: Crea un atributo XML a partir de una lista de expresiones de valor de fila.
    e.last_name AS "apellidos",
    d.department_name AS "departamento"))
FROM employees e
INNER JOIN departments d ON e.department_id = d.department_id
WHERE d.department_id = 80;


-- Mostrar el nombre, apellido, nombre de departamento, ciuda y pais de los empleados que son Managers.

/*
<?xml version = "1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xdb="http://xmlns.oracle.com/xdb" elementFormDefault="qualified">
    <xs:element name="managers">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="manager" minOccurs="0" maxOccurs="unbounded">
                    <xs:complexType>
                        <xs:sequence>
                            <xs:element name ="nombreCompleto">
                                <xs:complexType>
                                    <xs:sequence>
                                        <xs:element name = "nombre" type="xs:string"/>
                                        <xs:element name = "apellido" type="xs:string"/>
                                    </xs:sequence>
                                </xs:complexType>
                            </xs:element>
                            <xs:element name = "department" type="xs:string"/>
                            <xs:element name = "city" type="xs:string"/>
                           <xs:element name = "country" type="xs:string"/>
                        </xs:sequence>
                    </xs:complexType>
                </xs:element>
            </xs:sequence>
        </xs:complexType>
    </xs:element>
</xs:schema>
*/

SELECT XML("managers",
    XMLLAGG( -- XMLAGG: Agrega los valores de una expresión de valor de fila en una expresión de valor de fila de XML.
        XMLELEMENT("manager",
            XMLELEMENT("nombreCompleto",
                XMLFOREST( -- XMLFOREST: Crea un elemento XML a partir de una lista de expresiones de valor de fila.    
                    empleados.FIRST_NAME as "nombre",
                    empleados.LAST_NAME as "apellido"
                )),
                XMLELEMENT("department", departamento.DEPARTMENT_NAME),
                XMLELEMENT("city", localizacion.CITY),
                XMLELEMENT("country", pais.COUNTRY_NAME)
    )
)
) AS MANAGER
FROM employees empleados
INNER JOIN DEPARTMENTS departamento ON empleados.DEPARTMENT_ID = departamento.DEPARTMENT_ID
INNER JOIN JOBS jobs ON empleados.JOB_ID = jobs.JOB_ID
INNER JOIN LOCATIONS localizacion ON departamento.LOCATION_ID = localizacion.LOCATION_ID
INNER JOIN COUNTRIES pais ON localizacion.COUNTRY_ID = pais.COUNTRY_ID
WHERE jobs.JOB_TITLE LIKE '%Manager%'