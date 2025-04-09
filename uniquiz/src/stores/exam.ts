import { defineStore } from "pinia"
import { useTokenStore } from "./token"
import type { paper_qn, paper_qn_rep } from "./paper"
import { ref } from "vue"

export const useExamStore = defineStore('exam', () => {
    const qs = ref<paper_qn[]>()

    const req_qs = (paper_id: number) => {
        const token = useTokenStore().get_token()
        uni.request({
            url: "/api/paper/" + paper_id + "/qs",
            header: { 'Authorization': token }
        }).then((res: any) => {
            if (res.statusCode == 200) {
                const qn_rep: paper_qn_rep[] = res.data
                qs.value = qn_rep.map(e => ({
                    ...e,
                    options: e.options ? JSON.parse(e.options) : {},
                    answer: ''
                }))
            } else {
                uni.showToast({ title: res.data.toString() })
            }
        }).catch(err => {
            uni.showToast({ title: err })
        })
    }

    return { qs, req_qs }
})



export interface usr_ans {
    qn_id: number
    usr_ans: string
}