# PayClear - Desarrollo de Interfaces (DI)

Prototipo interactivo y especificacion tecnica del gestor de gastos compartidos y liquidacion multilateral en local (Local-First), desarrollado para el modulo de Desarrollo de Interfaces (2º DAM).

---

## 1. Descripcion General

PayClear es una solucion orientada a simplificar la division de gastos grupales y la optimizacion de balances en comunidades cerradas (pisos compartidos, viajes y colectivos). Funciona de forma 100% local, sin muros de pago, sin publicidad y sin requerir registro de cuentas.

---

## 2. Entregables del Sprint 1

* **Prototipo Interactivo en Figma:** [Acceso al prototipo navegable](https://www.figma.com/make/tw427B0LEsxsFf9xkGl7hG/Maquetacion-Vista-Principal-PI?t=nwGOOxNGUa5cHgF6-1)
* **Gestion del Proyecto:** [Tablero en GitHub Projects](https://github.com/users/jesuscabeza25-lab/projects/2/views/1)
* **Memoria Tecnica Detallada:** Consultar [SPRINT1.md](Sprint1.md) para la evaluacion formal de los criterios RA1 (arquitectura MVC, comparativa de librerias graficas, componente reutilizable y caso de prueba del algoritmo).

---


### 2.1. Alcance del Sprint 1

* Prototipado interactivo de alta fidelidad en Figma de la ventana principal y de la calculadora modal de tickets.
* Sistema de componentes reutilizables con variantes de estado para representar los saldos individuales (acreedor, deudor y neutral).
* Simulacion funcional de eventos y transiciones entre pantallas.
* Definicion formal de la arquitectura Modelo-Vista-Presentador (MVP) y el catalogo de clases para su implementacion en Java Swing durante el Sprint 2.

---

## 3. Entregables del Sprint 2

- **Implementacion Visual en Java Swing / FlatLaf:** Codigo fuente y formularios de las vistas en [PayClear-DI/PayClear/](PayClear/)
- **Prototipo Interactivo Actualizado en Figma:** [Acceso al prototipo navegable](https://www.figma.com/make/tw427B0LEsxsFf9xkGl7hG/Maquetacion-Vista-Principal-PI?t=nwGOOxNGUa5cHgF6-1)
- **Gestion y Seguimiento del Sprint:** [Tablero Kanban en GitHub Projects](https://github.com/users/jesuscabeza25-lab/projects/2/views/8)
- **Memoria Tecnica del Sprint 2:** Consultar [SPRINT2.md](Sprint2.md) para la evaluacion de los criterios de usabilidad, accesibilidad (WCAG 2.1 AA), aspecto visual, secuencia de control y cronograma.

---

### 3.1. Alcance del Sprint 2

- Definicion de criterios de usabilidad (ISO 9241-11, heuristicas de Nielsen), accesibilidad (WCAG 2.1 Nivel AA) y metricas de auditoria.
- Refinamiento de wireframes y mockups en Figma y maquetacion de las vistas y cuadros de dialogo en Java Swing con FlatLaf.
- Especificacion del diseno estructural (menus, ventanas, modales y atajos de teclado) y pautas de aspecto visual con paleta accesible, tipografias (`Manrope` y `DM Sans`) e iconos SVG.
- Documentacion de la secuencia de control (flujo de vistas `P1` a `P3.x`, validacion de errores inline, repintado reactivo en el EDT y bloqueo seguro de boveda) junto con el cronograma de ejecucion y diagrama de Gantt.

---

## 4. Alcance del Sprint 3 

- Desarrollo del JavaBean visual reutilizable `TarjetaSaldoParticipante` dentro del proyecto aislado `PayClearComponentes`, con cambio automatico de estado visual segun saldo (acreedor en verde, deudor en rojo y saldado en gris), propiedades ligadas (`PropertyChangeEvent`), emision de `ActionEvent` y soporte completo de accesibilidad (navegacion por teclado con `Tab`, `Intro` y `Espacio`, y descripciones para lectores de pantalla).
- Creacion y ejecucion de la bateria de pruebas unitarias aisladas (`TarjetaSaldoParticipanteTest`) mediante JUnit 4.13.2 + Hamcrest sobre el EDT de Swing (6/6 pruebas superadas sin depender de `VistaPrincipal`), validando valores por defecto, setters, colores por estado, control de nulos y disparo de eventos.
- Documentacion integral del componente y del paquete (`package-info.java`) con Javadoc cumpliendo las reglas estrictas de DocLint y codificacion UTF-8, junto con el empaquetado distribuible en `PayClearComponentes/dist/PayClearComponentes.jar` y la generacion de `dist/javadoc/index.html`.
- Integracion del `.jar` en las librerias de `PayClear` y en la paleta de NetBeans Matisse, sustituyendo las tarjetas estaticas de `VistaPrincipal` por instancias de `TarjetaSaldoParticipante` conectadas al filtrado de movimientos por participante, verificando el flujo completo con raton y teclado.

---

### 4.1. Entregables del Sprint 3

- **Libreria del Componente Reutilizable:** Proyecto independiente con el JavaBean `TarjetaSaldoParticipante`, pruebas unitarias, empaquetado `.jar` y documentacion HTML en [PayClearComponentes/](PayClearComponentes/)
- **Integracion en la Aplicacion Principal:** Vistas actualizadas con el componente en la paleta de NetBeans Matisse y Look & Feel FlatLaf en [PayClear/](PayClear/)
- **Gestion y Seguimiento del Sprint:** [Tablero Kanban en GitHub Projects](https://github.com/users/jesuscabeza25-lab/projects/2/views/11)
- **Memoria Tecnica del Sprint 3:** Consultar [Sprint3.md](Sprint3.md) para el detalle del desarrollo del componente, bateria de pruebas aisladas, generacion de Javadoc y empaquetado.


---

## 4. Estructura Proyectada de Paquetes (MVC)

```text
src/main/java/com/payclear/
|-- AplicacionPayClear.java
|-- componente/
|   `-- TarjetaSaldoParticipante.java
|-- controlador/
|   `-- ControladorPrincipal.java
|-- modelo/
|   |-- Gasto.java
|   `-- Participante.java
`-- vista/
    |-- DialogoDivisionRapida.form
    |-- DialogoDivisionRapida.java
    |-- VistaPrincipal.form
    `-- VistaPrincipal.java
```

---

## 5. Equipo de Desarrollo

* Guillermo Eugui Sanchez
* Jesus Cabeza
* Jose Luis Fernandez
