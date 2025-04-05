import { jwtDecode as jwt_decode } from "jwt-decode"
import { defineStore } from "pinia"
import { ref } from "vue"

export const useTokenStore = defineStore('token', () => {
    const name = ref<string>()
    const sub = ref<number>()
    const teacher = ref<boolean>()

    const save = (token: string) => {
        try {
            const decoded: token_type = jwt_decode(token)
            name.value = decoded.name
            sub.value = decoded.sub
            teacher.value = decoded.teacher
        } catch (err) {
            uni.showToast({ title: '解析token失败' })
        }

    }

    const get_token = () => {

        const token: string = uni.getStorageSync(
            'Authorization'
        )

        if (!token) {
            uni.redirectTo({ url: "/pages/user/login" })
            return
        }

        const decoded: token_type = jwt_decode(token)
        if (decoded.exp < Date.now() / 1000) {
            console.log("token已过期...")
            uni.redirectTo({ url: "/pages/user/login" })
            return
        }

        console.log("获取token...")
        return token
    }

    return { name, sub, teacher, save, get_token }
})

interface token_type {
    exp: any
    name: string
    sub: number
    teacher: boolean
}