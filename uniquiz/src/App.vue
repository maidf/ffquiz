<script setup lang="ts">
import { onHide, onLaunch, onShow } from "@dcloudio/uni-app"
import { useTokenStore } from "./stores/token"
import { storeToRefs } from "pinia"
onLaunch(() => {
    console.log("App Launch")
    const store = useTokenStore()
    const { get_token, save } = store
    const token = get_token()
    console.log(token)
    if (token) {
        save(token)
        const { teacher } = storeToRefs(store)
        const login_tag = uni.getStorageSync('login_tag')
        
        if (login_tag == "teacher" && teacher.value) {
            uni.redirectTo({ url: "/pages/tea/tea-home" })
        } else {
            uni.redirectTo({ url: "/pages/index/home" })
        }
    }

})
onShow((e: any) => {
    console.log("App Show")
})
onHide(() => {
    console.log("App Hide")
})
</script>
<style></style>
