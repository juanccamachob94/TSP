/**
 * Contiene los items estáticos provenientes de la metodología TSP
 */
package e_utilitaria;

import c_negocio.a_relacional.Criterio;
import c_negocio.a_relacional.Meta;
import c_negocio.a_relacional.MetaId;
import c_negocio.a_relacional.PyCr;
import c_negocio.a_relacional.Rol;
import c_negocio.b_no_relacional.atributo.AtributoCompuesto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TSP {

    public static final List fases = new ArrayList() {{
        add("Lanzamiento");
        add("Estrategia");
        add("Planeación");
        add("Requerimientos");
        add("Diseño");
        add("Implementación");
        add("Prueba");
        add("Postmortem");
    }};
    
   public static final List tareasAntesPlaneacion = new ArrayList() {{
       add(new ArrayList(){{
           add("Asignación de roles");
           add("Definición de metas");
       }});
       add(new ArrayList(){{
           add("Desarrollo de la estrategia");
       }});
   }};

    public static final List defectos = new ArrayList() {{
        add("10 - Documentación");
        add("20 - Sintaxis");
        add("30 - Paquete");
        add("40 - Asignación");
        add("50 - Interfaz");
        add("60 - Revisión");
        add("70 - Fecha");
        add("80 - Función");
        add("90 - Sistema");
        add("100 - Ambiente");
    }};

    public static final List prioridades = new ArrayList() {{
        add("H - Alto");
        add("M - Mediano");
        add("L - bajo");
    }};
    
    public static final List unidadesTamanio = new ArrayList(){{
        add("Funciones");
        add("Páginas");
        add("Líneas de código");
        add("Archivos");
    }};

    public static final List riesgoProblema = new ArrayList() {{
        add("Riesgo");
        add("Problema");
    }};

    public static final List coloresFases = new ArrayList() {{
        add("#ffcc80");
        add("#afb42b");
        add("#e6ee9c");
        add("#c5e1a5");
        add("#b9f6ca");
        add("#81d4fa");
        add("#ce93d8");
        add("#ef9a9a");
    }};

    public static final List etapas = new ArrayList() {{
        add("Lanzamiento y estrategia");

        add("Estrategia y planeación");
        add("Requerimientos");
        add("Plan de prueba de sistema");
        add("Inspección de requerimientos");
        add("Diseño de alto nivel");
        add("Plan de prueba de integración");
        add("Inspección de diseño de alto nivel (HLD)");
        add("Diseño detallado");
        add("Revisión de diseño detallado (DLD)");
        add("Desarrollo de prueba");
        add("Inspección de diseño detallado (DLD)");
        add("Código");
        add("Revisión de código");
        add("Compilación");
        add("Inspección de código");
        add("Prueba de unidad");
        add("Prueba de construcción e integración");
        add("Prueba de sistema");

        add("Documentación");
        add("Postmortem");
        
        add("Gerencia y miscelanea");
    }};

    public static final List rolesTSP = new ArrayList() {{
        add(new Rol("Líder de proyecto", "..."));
        add(new Rol("Gerente de desarrollo", "..."));
        add(new Rol("Gerente de calidad/proceso", "..."));
        add(new Rol("Gerente de soporte", "..."));
        add(new Rol("Gerente de planeación", "..."));
    }};
    
    public static final Rol instructor = new Rol("Instructor","..");
    
    public static final List criterios = new ArrayList(){{
        add(new Criterio("Día de referencia", "Día de la semana para entregar progreso del proyecto", "select", null));
        add(new Criterio("Unidad de tamaño", "Unidad de conteo de tamaño de código para el proyecto", "select", null));
    }};
    
    public static final List descripcionCriterios = new ArrayList(){{
        add("Día de semana en la que se va a hacer la evaluación del progreso");
        add("Unidad de tamaño para el software a desarrollar");
    }};
    
    public static final List metasTSPProyecto = new ArrayList(){{
        add(new Meta(new MetaId("Defectos encontrados en compilación"),true,new BigDecimal(80),"porcentaje","igual a"));
        add(new Meta(new MetaId("Defectos encontrados en la prueba de sistema"),true,new BigDecimal(0),"cantidad","igual a"));
        add(new Meta(new MetaId("Funciones de requerimientos al completar el ciclo"),false,new BigDecimal(100),"porcentaje","igual a"));
        add(new Meta(new MetaId("Error en el estimado de tamaño del producto"),true,new BigDecimal(20),"porcentaje","menor a"));
        add(new Meta(new MetaId("Error en las horas estimadas de desarrollo"),false,new BigDecimal(20),"cantidad","menor a"));
        add(new Meta(new MetaId("Información registrada del ciclo"),true,new BigDecimal(100),"porcentaje","igual a"));
        add(new Meta(new MetaId("Completar el desarrollo con un corto desfase de días"),false,new BigDecimal(4),"día(s)","igual a"));
    }};

    public static final List metasIntegranteTSP = new ArrayList(){{
        add(new Meta(new MetaId("Conseguir una evaluación en ayuda y soporte (PEER) mayor a"),true,new BigDecimal(3),"porcentaje","mayor a"));
        add(new Meta(new MetaId("Información personal registrada en el proyecto"),true,new BigDecimal(100),"porcentaje","igual a"));
        add(new Meta(new MetaId("Semanas para completar WEEK"),true,new BigDecimal(100),"porcentaje","igual a"));
        add(new Meta(new MetaId("Información personal del proyecto registrada en SUMP y SUMQ"),true,new BigDecimal(100),"porcentaje","igual a"));
        add(new Meta(new MetaId("Tareas de proyecto con información completa (plan y real) en el formato TASK"),true,new BigDecimal(100),"porcentaje","igual a"));
        add(new Meta(new MetaId("Promedio de defectos encontrados antes de la primera compilación"),false,new BigDecimal(70),"","mayor a"));
     }};
    
    public static final List metasRoles = new ArrayList(){{
        add(new ArrayList(){{
            add(new Meta(new MetaId("Construir y mantener un equipo efectivo"),false));
            add(new Meta(new MetaId("Motivar a todos los miembros del equipo a participar activamente en el proyecto"),false));
            add(new Meta(new MetaId("Resolver todos los problemas que exponen los integrantes del equipo"),false));
            add(new Meta(new MetaId("Mantener al instructor informado acerca del progreso del equipo"),false));
            add(new Meta(new MetaId("Desempeñarse efectivamente como facilitador de las reuniones de equipo"),false));
        }});
        add(new ArrayList(){{
            add(new Meta(new MetaId("Producir un producto superior"),false));
            add(new Meta(new MetaId("Utilizar por completo las habilidades de los integrantes"),false));
        }});
        add(new ArrayList(){{
            add(new Meta(new MetaId("Asegurar que todos los integrantes del equipo reportan de manera precisa y utilizan apropiadamente la información de TSP"),false));
            add(new Meta(new MetaId("El equipo sigue fielmente TSP y produce un producto de calidad"),false));
            add(new Meta(new MetaId("Todas las inspecciones de equipo son apropiadamente reportadas y moderadas"),false));
            add(new Meta(new MetaId("Todas las juntas de equipo se reportan de manera precisa y los reportes se almacenan en el cuaderno del proyecto"),false));
        }});
        add(new ArrayList(){{
            add(new Meta(new MetaId("El equipo tiene herramientas adecuadas y métodos para soportar el trabajo"),false));
            add(new Meta(new MetaId("No se hacen cambios no autorizados a los productos base"),false));
            add(new Meta(new MetaId("Todos los riesgos y problemas del equipo se registran en el lanzamiento de problemas y se reportan cada semana"),false));
            add(new Meta(new MetaId("El equipo cumple sus metas de reuso para el ciclo de desarrollo"),false));
        }});
        add(new ArrayList(){{
            add(new Meta(new MetaId("Reportar de manera exacta el estatus semanal del equipo"),false));
            add(new Meta(new MetaId("Producir un plan completo, preciso y exacto, para cada integrante y el equipo en general"),false));
        }});
    }};

    public static final String mensajeSeleccion = "Seleccione...";
    public static final String ciclo = "Ciclo";
    
    public static final List atrsDetallesGraficasCiclo = new ArrayList(){{
        add(new AtributoCompuesto("Tiempo en etapa","Comparación entre el tiempo planeado y real en cada etapa del ciclo actual",null));
        add(new AtributoCompuesto("Tamaño del producto","Comparación entre el tamaño planeado y real en cada etapa del ciclo actual",null));
        add(new AtributoCompuesto("Defectos inyectados","Comparación entre el valor planeado y real en defectos inyectados del ciclo actual",null));
        add(new AtributoCompuesto("Defectos removidos","Comparación entre el valor planeado y real en defectos removidos del ciclo actual",null));
        add(new AtributoCompuesto("PDF en compilación","Identificación porcentual de los defectos en la etapa de compilación",null));
        add(new AtributoCompuesto("PDF en prueba de unidad","Identificación porcentual de los defectos en la etapa de prueba de unidad",null));
        add(new AtributoCompuesto("PDF en construcción e integración","Identificación porcentual de los defectos en la etapa de construcción e integración",null));
        add(new AtributoCompuesto("PDF en prueba de sistema","Identificación porcentual de los defectos en la etapa de prueba de sistema",null));
        add(new AtributoCompuesto("Tiempo de arreglo de defectos","Comparación de tiempos de arreglo por defecto",null));
        add(new AtributoCompuesto("Valor planeado vs Valor ganado","Comparación semana a semana de VP y VG del ciclo",null));
        add(new AtributoCompuesto("Logro de metas","Logro de metas en el ciclo",null));
        add(new AtributoCompuesto("Defectos/Código","Información de defectos por código",null));
        add(new AtributoCompuesto("Rendimientos de fase","Rendimiento por cada etapa del ciclo",null));
        add(new AtributoCompuesto("Tasa de inyección de defectos","Porcentaje representativo de la tendencia a inyectar defectos",null));
        add(new AtributoCompuesto("Tasa de remoción de defectos","Porcentaje representativo de la tendencia a remover defectos",null));
    }};
    
    public static final List atrsDetallesGraficasPersonales = new ArrayList(){{
        add(new AtributoCompuesto("Tiempo en proyectos","Total de tiempo dedicado en cada proyecto",null));
        add(new AtributoCompuesto("Trabajo y dificultad","Promedio de puntuación total obtenida en trabajo y dificultad requeridas en cada proyecto",null));
        add(new AtributoCompuesto("Contribución general","Contribución general total en cada proyecto",null));
        add(new AtributoCompuesto("Ayuda y soporte","Ayuda y soporte total en cada proyecto",null));
        add(new AtributoCompuesto("Desempeño","Desempeño total en cada proyecto",null));
    }};
    
    public static List diasSemana = new ArrayList(){{
        add("Domingo");
        add("Lunes");
        add("Martes");
        add("Miércoles");
        add("Jueves");
        add("Viernes");
        add("Sábado");
    }};
    
    public static List opcionesCriterio(PyCr pyCr) {
        List opcs = null;
        switch (pyCr.getCriterio().getNombre()) {
            case "Día de referencia":
                opcs = diasSemana;
                break;
            case "Unidad de tamaño":
                opcs = new ArrayList();
                opcs.add("Pts. funcls");
                opcs.add("Líneas de código");
                break;
        }
        return opcs;
    }

    public static List tipoPartes = new ArrayList(){{
        add("sistema");
        add("producto");
        add("componente");
        add("modulo");
    }};
    
    public static List criteriosSalida = new ArrayList(){{
        add("Cada estudiante ha completado y sometido el formato INFO");
        add("Los equipos de desarrollo se forman y se asignan roles");
        add("El instructor describió los objetivos generales del producto");
        add("El instructor ha revisado y discutido el TSP y los objetivos de rol y equipo");
        add("El equipo se ha puesto de acuerdo en los objetivos del ciclo 1, tiempos de reuniones semanales y la tiempo para reportar la información semanal");
        add("Una estrategia completa y documentada");
        add("Estimados de tiempo y tamaño completos para todos los elementos del producto a producir durante el siguiente ciclo");
        add("Estimados completos y documentados para los productos producidos en ciclos subsecuentes de desarrollo");
        add("Procedimiento de manejo de configuración documentado");
        add("Riesgos y problemas ingresados al registro ITL");
        add("Diseño conceptual y formato STRAT completo");
        add("Cuaderno de proyecto actualizado");
        add("Completar los formatos TASK y SCHEDULE de equipo e ingenieros");
        add("Formatos SUMP y SUMQ completos");
        add("Actualizar el cuaderno del proyecto");
        add("Un documento SRS completo e inspeccionado y plan de prueba de sistema");
        add("Información de tiempo, defecto y tamaño ingresada en el sistema de soporte TSP");
        add("Cuaderno de proyecto actualizado");
        add("SDS completo e inspeccionado, también plan de prueba de integración");
        add("Estándares de diseño y glosario");
        add("SUMP y SUMQ actualizados");
        add("Componentes completos, inspeccionados y con su configuración controlada");
        add("Planes de unidad de prueba y materiales de soporte");
        add("Formatos SUMP, SUMQ, LOGD y LOGT actualizados");
        add("Cuaderno de proyecto actualizado");
        add("Formatos ITL y LOGD actualizados");
        add("Formatos PEER actualizados");
    }};
}