import { defineStore } from "pinia"
import { ref } from "vue"
import type { qn, qn_rep } from "./qn"
import { useTokenStore } from "./token"

export const useAnsStore = defineStore('ans', () => {
    const qn = ref<qn>()

    const req_qn = (bank_id: number) => {
        uni.request({
            url: "/api/ans/" + bank_id,
            header: { 'Authorization': useTokenStore().get_token() }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                const qn_rep: qn_rep = res.data
                qn.value = {
                    ...qn_rep,
                    options: qn_rep.options ? JSON.parse(qn_rep.options) : {}
                }
            }
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    return { qn, req_qn }
})