package org.example.jUnit.models;

import org.example.jUnit.exceptions.DineroInsuficiente;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @BeforeEach
    void initMetodoTest(){
        System.out.println("Iniciando el método para todos por igual");
    }

    @AfterEach
    void tearDown(){
        System.out.println("Finalizando el método para todos por igual");
    }
    @Test
    @DisplayName("Revisando el nombre de la cuenta")
    void test_nombre_cuenta() {
        Cuenta cuenta = new Cuenta("ivan", new BigDecimal("1000.123212"));
        String expected = "ivan";
        String actual = cuenta.getPersona();
        assertNotNull(actual, () -> "El nombre no puede ser null");
        assertEquals(expected, actual, "No coincide los nombres: " + expected + ", es diferente de: " + actual);
        assertTrue(actual.equals("ivan"));

    }

    @Test
    @DisplayName("Revisando el saldo de las cuentas")
    @Disabled
    void test_saldo_cuenta() {
        Cuenta cuenta = new Cuenta("Jonas", new BigDecimal("1111.22222"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1111.2222, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void test_referencia_cuenta() {
        //fail()
        Cuenta cuenta = new Cuenta("Naza", new BigDecimal("222.333"));
        Cuenta cuenta2 = new Cuenta("Sebas", new BigDecimal("222.333"));

        assertNotEquals(cuenta2, cuenta);
    }

    @Test
    void test_debito_cuenta() {
        Cuenta cuenta = new Cuenta("Ivan", new BigDecimal("1000.0000"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900.0000, cuenta.getSaldo().doubleValue());
        assertEquals("900.0000", cuenta.getSaldo().toPlainString());
    }

    @Test
    void test_credito_cuenta() {
        Cuenta cuenta = new Cuenta("Ivan", new BigDecimal("1000.0000"));
        cuenta.credito(new BigDecimal(500));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1500.0000, cuenta.getSaldo().doubleValue());
        assertEquals("1500.0000", cuenta.getSaldo().toPlainString());
    }

    @Test
    void test_saldo_insuficiente() {
        Cuenta cuenta = new Cuenta("Ivan", new BigDecimal("1000.0000"));
        Exception exception = assertThrows(DineroInsuficiente.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Saldo Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void test_tranferir_dinero_cuentas() {
        Cuenta cuenta = new Cuenta("Ivan", new BigDecimal("1000.0000"));
        Cuenta cuenta2 = new Cuenta("Naza", new BigDecimal("1500.0000"));

        Banco banco = new Banco("BBVA");
        banco.transferir(cuenta2, cuenta, new BigDecimal(500));
        assertEquals("1000.0000", cuenta2.getSaldo().toPlainString());
        assertEquals(1500.0000, cuenta.getSaldo().doubleValue());
    }

    @Test
    void test_relacion_banco_cuentas() {
        Cuenta cuenta = new Cuenta("Ivan", new BigDecimal("1000.0000"));
        Cuenta cuenta2 = new Cuenta("Naza", new BigDecimal("1500.0000"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta);
        banco.addCuenta(cuenta2);

        banco.setNombre("BBVA");
        banco.transferir(cuenta2, cuenta, new BigDecimal(500));

        assertAll(
                () -> {
                    assertEquals("1000.0000", cuenta2.getSaldo().toPlainString());
                },
                () -> {
                    assertEquals(1500.0000, cuenta.getSaldo().doubleValue());
                },
                () -> {
                    assertEquals(2, banco.getCuentas().size());
                },
                () -> {
                    assertEquals("BBVA", cuenta.getBanco().getNombre());
                },
                () -> {
                    assertEquals("Ivan", banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Ivan"))
                            .findFirst()
                            .get().getPersona()
                    );
                });


        assertTrue(banco.getCuentas().stream()
                .filter(c -> c.getPersona().equals("Ivan"))
                .findFirst()
                .isPresent()
        );

        assertTrue(banco.getCuentas().stream()
                .anyMatch(c -> c.getPersona().equals("Naza"))
        );
    }
}