package nicestring

fun String.isNice(): Boolean = process(this)


fun process(string: String) : Boolean {

    var count = 0

    if (processRuleOne(string)) {
        count ++
    }

    if(processRuleTwo(string)) {
        count ++
    }

    if(processRuleThree(string)) {
        count++
    }

    return count >= 2
}

fun processRuleOne(string:String) : Boolean {

    return !(string.contains("ba") ||
            string.contains("bu") ||
            string.contains("be"))
}

fun processRuleTwo(string: String) : Boolean {
    var vowelCount = 0

    for (i in string.indices) {

        if(string[i] == 'a'
            || string[i] == 'e'
            || string[i] == 'i'
            || string[i] == 'o'
            ||string[i] == 'u' ) {

            vowelCount++
        }
    }

    return vowelCount >= 3
}

fun processRuleThree(string:String) : Boolean {

    var doubleLetter = false

    for (i in 0 until string.length - 1) {

        if(string[i] == string[i + 1]){
            doubleLetter = true
            break
        }
    }

    return doubleLetter
}