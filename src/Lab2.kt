import java.io.File
import java.util.*

// Файл с зашифрованным текстом
private const val secretFileName = "src/Lab2/ciphered.txt"
// Файл для записи расшифрованного текста
private  const val  textFileName = "src/Lab2/text.txt"

const val codePhrase = "Совершенно секретно"

/*
    Напишите программу дешифрования текста, зашифро
    ванного методом перестановки битов в каждом байте, если из
    вестно, что открытый текст должен начинаться с известной под
    строки, например: "Совершенно секретно"
 */

fun main() {
    // Получаем шифрованный массив
    val boolArray = saveEncrypted()
    // Дешифруем
    tryToDecrypt(boolArray)
}

// Дешифруем
fun tryToDecrypt(bools: BooleanArray) {
    // Получаем, как массивом булинов выглядит кодовая фраза
    val codePhraseInBooleanArray = toBooleanArray(codePhrase.toByteArray())
    // Ищем, на сколько был сдвинут массив булинов
    val bytePushNumber = findSubArrayInBooleanArray(codePhraseInBooleanArray, bools)
    // Двигаем, чтобы восстановить исходный массив
    val unpushed = pushBooleanArray(bools, bools.size - bytePushNumber)
    // Получаем массив в байтах
    val newBytes = toByteArray(unpushed)
    // Пишем его в файл
    File(textFileName).writeBytes(newBytes)
}

// Ищем, на сколько был сдвинут массив булинов
fun findSubArrayInBooleanArray(subArray: BooleanArray, bools: BooleanArray): Int {
    // Создаем массив для поиска и заполняем его двумя копиями массива булинов, чтобы
    // быстрее находить комбинации, если часть в конце, а часть в начале, чтобы найти начало подсписка, котрое находится в списке
    val newBools = BooleanArray(bools.size * 2)

    for (b in bools.indices) {
        newBools[b] = bools[b]
    }
    for (b in bools.indices) {
        newBools[b + bools.size] = bools[b]
    }

    // Ищем подмассив в массиве
    for (i in newBools.indices) {
        var fit = true
        for (j in subArray.indices) {
            if (newBools[(i + j) % newBools.size] != subArray[j % subArray.size]) {
                fit = false
            }
        }
        if (fit) {
            return i
        }
    }
    // Возвращаем -1, если ничего не нашли
    return -1
}

// Получаем шифрованный массив
fun saveEncrypted(): BooleanArray {
    // Создаем фразу для шифрования
    val text = "$codePhrase hello world"
    // Получаем её битами
    val boolArray = toBooleanArray(text.toByteArray())
    // Получаем уже сдвинутый массив булинов
    val pushed = pushBooleanArray(boolArray, 7)
    // Сохраняем
    File(secretFileName).writeBytes(toByteArray(pushed))
    // Возвращаем
    return pushed
}

// Двигаем массив
fun pushBooleanArray(bools: BooleanArray, bytesToPush: Int): BooleanArray {
    // Делаем копию массива
    var booleanArray = bools.copyOf()

    for (i in 0 until bytesToPush) {
        val booleanCacheArray = booleanArray.copyOf()
        for (b in booleanCacheArray.indices) {
            // Двигаем
            booleanCacheArray[(b + 1) % (booleanCacheArray.size)] = booleanArray[b]
        }
        booleanArray = booleanCacheArray
    }
    return booleanArray
}

// Список строк преобразует в одну строку
private fun listToString(list: List<String>): String {
    val stringBuilder = StringBuilder()

    for (line in list) {
        stringBuilder.append(line)
    }
    return stringBuilder.toString()
}

// Читает список строчек из файла
private fun readFile(fileName: String): String = listToString(File(fileName).useLines { it.toList() })

// Записывает строчку в файл
private fun saveToFile(string: String, fileName: String) {
    val file = File(fileName)
    file.writeText(string)
}
//переводит список байтов в список битов
fun toBooleanArray(bytes: ByteArray): BooleanArray {
    val bits: BitSet = BitSet.valueOf(bytes)
    val bools = BooleanArray(bytes.size * 8)
    var i: Int = bits.nextSetBit(0)
    while (i != -1) {
        bools[i] = true
        i = bits.nextSetBit(i + 1)
    }
    return bools
}
//переводит список битов в список байтов
fun toByteArray(bools: BooleanArray): ByteArray {
    val bits = BitSet(bools.size)
    for (i in bools.indices) {
        if (bools[i]) {
            bits.set(i)
        }
    }
    val bytes = bits.toByteArray()
    return if (bytes.size * 8 >= bools.size) {
        bytes
    } else {
        Arrays.copyOf(bytes, bools.size / 8 + if (bools.size % 8 == 0) 0 else 1)
    }
}