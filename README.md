# Bitcoin Script Interpreter - Proyecto 1 (Fase 2)

## Descripción
Este proyecto es un intérprete de Bitcoin Script en Java que procesa instrucciones tipo OPcode usando una pila (*stack*). Simula el comportamiento básico de la máquina de scripts de Bitcoin para ejecutar y validar scripts, incluyendo un ejemplo estilo P2PKH (Pay to Public Key Hash).

En la **Fase 2** el intérprete se amplía agregando:
- **Más OPcodes** (aritméticos, lógicos, comparaciones, criptografía extra y verificación)
- **Control de flujo** con `OP_IF`, `OP_NOTIF`, `OP_ELSE`, `OP_ENDIF`
- Ejecución de **múltiples scripts** desde un mismo archivo, separados por líneas (se usa internamente el token `NL`)

---

## Autores
- **Andrés Pineda** - 25212 - pin25212@uvg.edu.gt  
- **Alejandro Sagastume** - 25257 - sag25257@uvg.edu.gt  
- **Jimena Vásquez** - 25092 - vas25092@uvg.edu.gt  

---

## Características

### Fase 1 (base)
- Lectura de scripts desde archivo `.txt`
- Manejo de pila (stack) para operar con datos
- Soporte de datos hexadecimales en formato `<...>`
- Validación simplificada estilo P2PKH (ejemplo con `DUP`, `HASH160`, `EQUALVERIFY`, `CHECKSIG`)

### Fase 2 (implementada)
- Control de flujo: `OP_IF`, `OP_NOTIF`, `OP_ELSE`, `OP_ENDIF`
- Lógica: `OP_BOOLAND`, `OP_BOOLOR`, `OP_NOT`
- Aritmética: `OP_ADD`, `OP_SUB`
- Comparaciones: `OP_LESSTHAN`, `OP_GREATERTHAN`, `OP_LESSTHANOREQUAL`, `OP_GREATERTHANOREQUAL`, `OP_NUMEQUALVERIFY`
- Criptografía extra: `OP_SHA256`, `OP_HASH256`
- Verificación extra: `OP_CHECKSIGVERIFY`
- Operaciones de stack: `OP_SWAP`, `OP_OVER`

---

## OPcodes soportados (implementación actual)

### Push / datos
- `OP_0`
- `OP_1`
- `OP_2` a `OP_16` *(se manejan como caso especial dentro del intérprete)*
- `OP_PUSHDATA` *(datos hex entre `< >`)*

### Stack
- `OP_DUP`
- `OP_DROP`
- `OP_SWAP`
- `OP_OVER`

### Igualdad / verificación
- `OP_EQUAL`
- `OP_EQUALVERIFY`
- `OP_VERIFY` *(manejado directamente por el intérprete)*
- `OP_NUMEQUALVERIFY`
- `OP_RETURN` *(detiene la ejecución con error)*

### Criptografía y firmas
- `OP_HASH160`
- `OP_SHA256`
- `OP_HASH256`
- `OP_CHECKSIG`
- `OP_CHECKSIGVERIFY`

### Aritmética y lógica
- `OP_ADD`
- `OP_SUB`
- `OP_NOT`
- `OP_BOOLAND`
- `OP_BOOLOR`

### Comparaciones numéricas
- `OP_LESSTHAN`
- `OP_GREATERTHAN`
- `OP_LESSTHANOREQUAL`
- `OP_GREATERTHANOREQUAL`

### Control de flujo
- `OP_IF`
- `OP_NOTIF`
- `OP_ELSE`
- `OP_ENDIF`

---

## Estructura del Proyecto

> **Importante:** trabajar/compilar desde la carpeta `demo/`.

```
Proyecto-1-Algoritmos/
│
├── demo/
│   ├── src/
│   │   ├── main/
│   │   │   └── java/
│   │   │       └── org/
│   │   │           └── script/
│   │   │               ├── Main.java                # Punto de entrada
│   │   │               ├── Interpreter.java         # Intérprete principal
│   │   │               ├── OPcodeOperations.java    # Implementación de OPcodes
│   │   │               ├── OPcode.java              # Interface funcional para OPcodes
│   │   │               ├── Stack.java               # Estructura de datos Stack
│   │   │               ├── CryptoOperations.java    # Operaciones criptográficas
│   │   │               ├── TXTReader.java           # Lector del archivo de scripts
│   │   │               └── Operaciones.txt          # Scripts de prueba (varias líneas)
│   │   │
│   │   └── test/
│   │       └── java/
│   │           └── org/
│   │               └── script/
│   │                   ├── OPcodesTest.java         # Pruebas de opcodes
│   │
│   └── pom.xml                                      # Configuración Maven (incluye JUnit)
│
└── README.md
```

---

## Requisitos
- **Java:** JDK 17 o superior  
- **Maven:** 3.6+ (recomendado)  
- IDE recomendado: IntelliJ IDEA / Eclipse / VS Code  

---

## Instalación y Ejecución

### 1) Clonar el repositorio
```bash
git clone https://github.com/Nenerox/Proyecto-1-Algoritmos.git
cd Proyecto-1-Algoritmos/demo
```

### 2) Compilar (con Maven)
```bash
mvn clean compile
```

### 3) Ejecutar (sin Maven)
```bash
# Compilar (desde demo/)
javac -d bin src/main/java/org/script/*.java

# Ejecutar
java -cp bin org.script.Main
```

---

## Archivo de scripts: `Operaciones.txt`

El programa (desde `Main.java`) lee el archivo:

`src\main\java\org\script\Operaciones.txt`

### ¿Cómo se procesan varias líneas?
- `TXTReader` lee el archivo **línea por línea**
- ignora comentarios (líneas que comienzan con `#`) y líneas vacías
- concatena cada línea agregando un separador interno `NL`
- el `Interpreter` detecta `NL` para:
  - imprimir resultado del script de esa línea
  - limpiar pilas
  - continuar con la siguiente línea

### Ejemplo de contenido (formato)
Un script P2PKH típico se ve así:

```
<firma> <clave_publica> OP_DUP OP_HASH160 <hash_esperado> OP_EQUALVERIFY OP_CHECKSIG
```

---

## Formato del Script
- **Datos hexadecimales:** entre `< >` (ejemplo: `<ABCDEF>`)
- **OPcodes:** en mayúsculas con prefijo `OP_`
- **Comentarios:** líneas que empiezan con `#` se ignoran
- **Espacios:** separan instrucciones y datos

---

## Cómo Funciona (resumen)
Flujo general:

```
Usuario → Main → TXTReader → Interpreter → OPcodeOperations → Stack
```

El `Interpreter`:
- registra los OPcodes en un `Map<String, OPcode>`
- procesa cada token del script en orden
- reconoce:
  - datos hex `<...>` (push al stack)
  - `OP_2` a `OP_16`
  - control de flujo (`OP_IF`, `OP_NOTIF`, `OP_ELSE`, `OP_ENDIF`)
  - verificaciones (`OP_VERIFY`)
- imprime la traza del stack después de cada instrucción

---

## Solución de Problemas

### Error: "FileNotFoundException" / no se lee `Operaciones.txt`
**Causa común:** ejecutar desde un directorio distinto a `demo/` (y la ruta relativa deja de coincidir).  
**Solución:** asegúrate de estar en `Proyecto-1-Algoritmos/demo` antes de compilar/ejecutar.

### Error: "Instrucción desconocida"
**Causa:** opcode no implementado o mal escrito.  
**Solución:** revisa que el token coincida exactamente con los opcodes soportados listados arriba.

### Error: "OP_EQUALVERIFY failed" / "OP_NUMEQUALVERIFY failed" / "OP_CHECKSIGVERIFY failed"
**Causa:** la condición verificada resultó falsa.  
**Solución:** revisa el orden de los pushes y de las operaciones (y que los datos sean correctos).

---

## Curso
**CC2003 - Sección 20 - Algoritmos y Estructura de Datos**  
Universidad del Valle de Guatemala  
