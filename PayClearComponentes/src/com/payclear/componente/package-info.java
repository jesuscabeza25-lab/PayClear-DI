/**
 * Componentes visuales reutilizables desarrollados para la aplicación PayClear.
 * <p>
 * Este paquete agrupa los JavaBeans de Swing encargados de representar
 * información económica de los participantes de forma accesible e interactiva,
 * independientes de la capa de persistencia y de las ventanas principales.
 * </p>
 *
 * <h2>Componentes incluidos</h2>
 * <ul>
 *   <li>{@link com.payclear.componente.TarjetaSaldoParticipante}: tarjeta de
 *       saldo individual con código de colores, soporte de teclado, eventos
 *       {@code ActionEvent} y propiedades ligadas ({@code PropertyChangeEvent}).</li>
 * </ul>
 *
 * <h2>Requisitos de uso</h2>
 * <ul>
 *   <li>Java 11 o superior con soporte Swing.</li>
 *   <li>Saldos con valor absoluto menor o igual que {@code 0.005} se consideran saldados.</li>
 * </ul>
 *
 * @author Equipo PayClear
 */

package com.payclear.componente;
