import { defineStore } from "pinia"
import { useTokenStore } from "./token"

export const useUsrStore = defineStore('usr', () => {

    const logout = () => {
        const token = useTokenStore().get_token()
        uni.request({
            url: "/api/usr/logout",
            header: { 'Authorization': token }
        })
        uni.redirectTo({ url: "/pages/user/login" })
    }

    const logoff = () => {
        const token = useTokenStore().get_token()
        uni.request({
            url: "/api/email/logoff",
            header: { 'Authorization': token }
        }).then(res => {
            if (res.statusCode == 200) {
            } else {
                uni.showToast({ title: res.data.toString(), icon: 'none' })
            }
        }).catch(err => uni.showToast({ title: err, icon: 'none' }))
    }

    return { logout, logoff }
})