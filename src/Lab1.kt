import java.io.File

// Файл с зашифрованным текстом
private const val secretFileName = "src/Lab1/secret.txt"
// Файл для записи расшифрованного текста
private const val textFileName = "src/Lab1/text.txt"
// Файл с оригинальным текстом
const val originalFileName = "src/Lab1/original.txt"

fun main() {

// Создаем решетку, забрасываем массив для шифрования/расшифрования
// Эта решетка позволит записать лишь 5 символов (тк 5 true утверждений в массиве)
    val grille1 = Grille(booleanArrayOf(true, false, true, true, false, true, true, true, true, false, true, true, true, true, true, false))

// Читаем текст из original.txt.txt
    val originalText = readFile(originalFileName)

// Шифруем и сохраняем в secret.txt
    saveToFile(grille1.encode(originalText), secretFileName)

// Читаем зашифрованный текст из secret.txt
    val encodedText = readFile(secretFileName)

// Расшифровываем и сохраняем в text.txt
    saveToFile(grille1.decode(encodedText), textFileName)
}

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

// Решетка, array - массив bool-переменных (1 или 0)
class Grille(private val array: BooleanArray) {

    // Расшифровка, на вход - зашифрованная строка
    fun decode(inputString: String): String {
// Создаем копию массива для переворота
        var newArray = array
// Создаем "строителя" строк
        val stringBuilder = StringBuilder()
// Проходимся по индексам массива
        for (i in newArray.indices) {
// Если 1, то добавляем к строителю
            if (newArray[i]) {
                stringBuilder.append(inputString[i])
            }
            newArray = rotateGrille(newArray)
        }
// Возвращаем
        return stringBuilder.toString()
    }

    // Шифровка, на вход - просто строка
    fun encode(inputString: String): String {
// Создаем копию массива для переворота
        var newArray = array
// Создаем временную переменную (чтобы можно было изменять)
        var inputStringToRedo = inputString
// "Строитель" строк
        val stringBuilder = StringBuilder()
// Идем по индексам массива
        for (i in newArray.indices) {
            if (newArray[i]) {
// Если нужно вставить нужную букву, то добавляем 0 элемент строки
                stringBuilder.append(inputStringToRedo[0])
// И укорачиваем с начала
                inputStringToRedo = inputStringToRedo.subSequence(1, inputStringToRedo.length).toString()
            } else {
// Иначе добавляем случайную букву
                stringBuilder.append(('A'..'z').map { it }.shuffled().subList(0, 1).joinToString(""))
            }
            newArray = rotateGrille(newArray)
        }
// Возвращаем получившуюся строчку
        return stringBuilder.toString()
    }

    companion object {
        // Поворот на 90 градусов по часовой стрелке
        fun rotateGrille(array: BooleanArray): BooleanArray {
            val list = List(array.size) {
                false
            }.toMutableList()
// Размерность решетки
// Находим корень от размера массива
            val dimens = Math.sqrt(array.size.toDouble()).toInt()

            for (i in 0 until dimens) {
                for (j in 0 until dimens) {
// Переворачиваем ряды в колонки
                    val index = i * dimens + j
                    val newIndex = j * dimens + i
                    list[newIndex] = array[index]
                }
            }
            val newArray = list.toTypedArray()
            return newArray.toBooleanArray()
        }
    }
}