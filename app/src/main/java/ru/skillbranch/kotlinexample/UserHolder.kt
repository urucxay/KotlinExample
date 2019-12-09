package ru.skillbranch.kotlinexample

import ru.skillbranch.kotlinexample.extentions.trimPhone

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
            map[login.trimPhone()]?.let { user ->
                if (user.checkPassword(password)) user.userInfo
                else null
            }
        }
    }

    fun requestAccessCode(login: String) {
        if (map.contains(login.trimPhone())) {
            map[login.trimPhone()]?.apply {
                val code = generateAccessCode()
                passwordHash = encrypt(code)
                accessCode = code
            }!!.also { map[login] = it }
        }
    }

    fun importUsers(list: List<String>): List<User> {
        val users = mutableListOf<User>()
        list.forEach { string ->
            val userFields = string.split(";")
            val user = User.makeUserFromImport(
                fullName = userFields[0].trim(),
                email = userFields[1].ifEmpty { null },
                passwordInfo = userFields[2].ifEmpty { null },
                phone = userFields[3].ifEmpty { null }
            )
            map[user.login] = user
            users.add(user)
        }
        return users
    }

}