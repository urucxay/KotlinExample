package ru.skillbranch.kotlinexample

object UserHolder {
    val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        return User.makeUser(fullName = fullName, email = email, password = password)
            .also { user ->
                if (!map.containsKey(user.login)) map[user.login] = user
                else throw IllegalArgumentException("A user with this email already exists")
            }
    }

    fun registerUserByPhone(
        fullName: String,
        rawPhone: String
    ): User {
        return User.makeUser(fullName = fullName, phone = rawPhone)
            .also { user ->
                if (!map.containsKey(user.login)) map[user.login] = user
                else throw IllegalArgumentException("A user with this phone already exists")
            }
    }

    fun loginUser(login: String, password: String): String? {
        return if (login.contains("@")) {
            map[login.trim()]?.let { user ->
                if (user.checkPassword(password)) user.userInfo
                else null
            }
        } else {
            map[login.replace("[^+\\d]".toRegex(), "")]?.let {user ->
                if (user.checkPassword(password)) user.userInfo
                else null
            }
        }
    }

    fun requestAccessCode(login: String) {
        map[login.replace("[^+\\d]".toRegex(), "")]?.apply {
            val code = generateAccessCode()
            passwordHash = encrypt(code)
            accessCode = code
        }!!.also { map[login] = it }
    }

}