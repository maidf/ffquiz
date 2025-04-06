<template>
    <view class="content">
        <uni-card title="创建题库" thumbnail="/static/logo.png">
            <view class="uni-body">
                <uni-forms :modelValue="bank">
                    <uni-forms-item label="题库名称" name="name">
                        <uni-easyinput type="text" v-model="bank.name" />
                    </uni-forms-item>
                    <uni-forms-item label="题库标签" name="subject">
                        <uni-easyinput type="text" v-model="bank.subject" />
                    </uni-forms-item>
                </uni-forms>
                <button size="mini" @click="add_bank(bank)">添加</button>
            </view>
        </uni-card>
    </view>
</template>

<script lang="ts" setup>
import { useBankStore, type bank } from '@/stores/bank'
import { ref } from 'vue'



const bank = ref<bank>({
    id: 0,
    name: '',
    subject: '',
    creator: '',
    create_time: new Date
})

const { add_bank } = useBankStore()

</script>

<style lang="scss" scoped></style>