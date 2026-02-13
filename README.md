# Bitcoin Script Interpreter - Proyecto 1

## Descripción
Implementación de un intérprete de Bitcoin Script en Java que ejecuta y valida transacciones de Bitcoin. Este proyecto simula el funcionamiento de la máquina virtual de Bitcoin, procesando instrucciones de script y validando transacciones P2PKH (Pay to Public Key Hash).

## Autores
- **Andrés Pineda** - 25212 - [pin25212@uvg.edu.gt]
- **Alejandro Sagastume** - 25257 - [vas25092@uvg.edu.gt]
- **Jimena Vásquez** - 25092 - [sag25257@uvg.edu.gt]

## Características

### Fase 1 (Implementada)
- Lectura de scripts desde archivo `.txt`
- Interpretación de OPcodes básicos de Bitcoin
- Validación de transacciones P2PKH
- Manejo de stack (pila) para operaciones
- Operaciones criptográficas (HASH160, CHECKSIG)

### OPcodes Implementados
| OPcode | Descripción |
|--------|-------------|
| `OP_0` | Push valor 0 (false) al stack |
| `OP_1` | Push valor 1 (true) al stack |
| `OP_2` - `OP_16` | Push valores 2-16 al stack |
| `OP_PUSHDATA` | Push datos hexadecimales al stack |
| `OP_DUP` | Duplica el elemento superior del stack |
| `OP_DROP` | Elimina el elemento superior del stack |
| `OP_EQUAL` | Compara dos elementos superiores del stack |
| `OP_EQUALVERIFY` | OP_EQUAL + verifica resultado |
| `OP_HASH160` | Aplica SHA-256 + RIPEMD-160 |
| `OP_CHECKSIG` | Verifica firma digital |

## Estructura del Proyecto

```
Proyecto-1-Algoritmos/
│
├── demo/            ← TRABAJAR SIEMPRE DESDE AQUÍ
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── org/
│   │               └── script/
│   │                   ├── Main.java                  # Punto de entrada
│   │                   ├── Interpreter.java           # Intérprete principal
│   │                   ├── OPcodeOperations.java      # Implementación de OPcodes
│   │                   ├── OPcode.java                # Interface para OPcodes
│   │                   ├── Stack.java                 # Estructura de datos Stack
│   │                   ├── CryptoOperations.java      # Operaciones criptográficas
│   │                   └── TXTReader.java             # Lector de archivos
│   │
│   ├── script.txt                                     # Script de ejemplo
│   └── pom.xml                                        # Configuración Maven
│
└── README.md
```

## Requisitos

- **Java:** JDK 17 o superior
- **Maven:** 3.6+ (opcional)
- **IDE recomendado:** IntelliJ IDEA, Eclipse, o VS Code

## Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone https://github.com/Nenerox/Proyecto-1-Algoritmos.git
cd Proyecto-1-Algoritmos/demo
```

### 2. Compilar el proyecto

#### Opción A: Con Maven
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="org.script.Main"
```

#### Opción B: Sin Maven
```bash
# Compilar
javac -d bin src/main/java/org/script/*.java

# Ejecutar
java -cp bin org.script.Main
```

### 3. Usar tu propio script
Edita el archivo `script.txt` con tu Bitcoin Script:

```
<firma> <clave_publica> OP_DUP OP_HASH160 <hash_esperado> OP_EQUALVERIFY OP_CHECKSIG
```

## Formato del Script

### Ejemplo de script P2PKH válido:
```
<0101010101...> <0202020202...> OP_DUP OP_HASH160 <A9A8D4AE65DE409A1EF6AB6608F0CE3FED019438> OP_EQUALVERIFY OP_CHECKSIG
```

### Reglas de formato:
- **Datos hexadecimales:** Entre `< >` (ejemplo: `<ABCDEF>`)
- **OPcodes:** En mayúsculas con prefijo `OP_`
- **Comentarios:** Líneas que empiezan con `#` son ignoradas
- **Espacios:** Pueden usarse para separar instrucciones

## Cómo Funciona

### 1. Flujo de ejecución
```
Usuario → Main → TXTReader → Interpreter → OPcodeOperations → Stack
```

### 2. Proceso de validación P2PKH
1. **Push firma y clave pública** al stack
2. **OP_DUP**: Duplica la clave pública
3. **OP_HASH160**: Hashea la clave pública
4. **Push hash esperado** al stack
5. **OP_EQUALVERIFY**: Verifica que los hashes coincidan
6. **OP_CHECKSIG**: Verifica la firma digital

### 3. Estructura del Stack (LIFO)
```
TOP → [último elemento]
      [elemento 2]
      [primer elemento] → BOTTOM
```

## Fase 2 (Próximamente)

### OPcodes a implementar:
- `OP_SWAP` - Intercambia dos elementos superiores
- `OP_OVER` - Copia el segundo elemento al top
- `OP_NOT` - Invierte booleano
- `OP_BOOLAND` - AND lógico
- `OP_BOOLOR` - OR lógico
- `OP_ADD` / `OP_SUB` - Operaciones aritméticas
- `OP_LESSTHAN` / `OP_GREATERTHAN` - Comparaciones numéricas
- `OP_NUMEQUALVERIFY` - Verificación de igualdad numérica

## Solución de Problemas

### Error: "FileNotFoundException"
**Causa:** El archivo `script.txt` no se encuentra
**Solución:** Verifica que `script.txt` esté en el directorio raíz del proyecto

### Error: "OP_EQUALVERIFY failed"
**Causa:** El hash de la clave pública no coincide con el hash esperado
**Solución:** Verifica que los datos hexadecimales sean correctos

### Error: "Instrucción desconocida"
**Causa:** OPcode no implementado o mal escrito
**Solución:** Revisa la lista de OPcodes soportados



## Curso

**CC2003 - Sección 20 - Algoritmos y Estructura de Datos**  
Universidad del Valle de Guatemala  
Hoja de Trabajo No. 2
