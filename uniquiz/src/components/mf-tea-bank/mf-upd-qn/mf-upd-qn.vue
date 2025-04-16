<template>
    <view class="content">
        <uni-card title="修改题目" thumbnail="/static/logo.png">
            <view class="uni-body">
                <uni-forms :modelValue="qn">
                    <uni-forms-item label="题目类型" name="type">
                        <uni-data-checkbox v-model="qn.type" :localdata="type" />
                    </uni-forms-item>
                    <uni-forms-item label="题目内容" name="content">
                        <uni-easyinput type="textarea" autoHeight v-model="qn.content" />
                    </uni-forms-item>
                    <view v-if="qn.type == qn_type.SINGLE_CHOICE || qn.type == qn_type.MULTIPLE_CHOICE">
                        <uni-forms-item label="选项 A">
                            <uni-easyinput type="text" v-model="qn.options.A" />
                        </uni-forms-item>
                        <uni-forms-item label="选项 B">
                            <uni-easyinput type="text" v-model="qn.options.B" />
                        </uni-forms-item>
                        <uni-forms-item label="选项 C">
                            <uni-easyinput type="text" v-model="qn.options.C" />
                        </uni-forms-item>
                        <uni-forms-item label="选项 D">
                            <uni-easyinput type="text" v-model="qn.options.D" />
                        </uni-forms-item>
                    </view>
                    <uni-forms-item v-if="qn.type == qn_type.MULTIPLE_CHOICE" label="题目答案" name="answer">
                        <uni-data-checkbox multiple v-model="qn.answer" :localdata="ans" />
                    </uni-forms-item>
                    <uni-forms-item v-else-if="qn.type == qn_type.SINGLE_CHOICE" label="题目答案" name="answer">
                        <uni-data-checkbox v-model="qn.answer" :localdata="ans" />
                    </uni-forms-item>
                    <uni-forms-item v-else-if="qn.type == qn_type.TRUE_FALSE" label="题目答案" name="answer">
                        <uni-data-checkbox v-model="qn.answer" :localdata="select" />
                    </uni-forms-item>
                    <uni-forms-item v-else label="题目答案" name="answer">
                        <uni-easyinput type="text" v-model="qn.answer" />
                    </uni-forms-item>
                    <uni-forms-item label="题目解析" name="ana">
                        <uni-easyinput type="textarea" autoHeight v-model="qn.ana" />
                    </uni-forms-item>
                    <uni-forms-item label="题目难度" name="diff">
                        <uni-data-checkbox v-model="qn.diff" :localdata="diff" />
                    </uni-forms-item>
                </uni-forms>
                <button size="mini" @click="upd_qn(qn)">更新</button>
            </view>
        </uni-card>
    </view>
</template>

<script lang="ts" setup>
import type { qn } from '@/stores/qn'
import { qn_diff, qn_type, useQnStore } from '@/stores/qn'
import { ref } from 'vue'

const { old_qn } = defineProps(['old_qn'])

const type = ref([
    {
        text: '单选',
        value: qn_type.SINGLE_CHOICE
    },
    {
        text: '多选',
        value: qn_type.MULTIPLE_CHOICE
    },
    {
        text: '填空',
        value: qn_type.FILL_BLANK
    },
    {
        text: '判断',
        value: qn_type.TRUE_FALSE
    },
])

const diff = ref([
    {
        text: '简单',
        value: qn_diff.EASY
    },
    {
        text: '中等',
        value: qn_diff.MEDIUM
    },
    {
        text: '困难',
        value: qn_diff.HARD
    },
])

const ans = ref([
    {
        text: 'A',
        value: 'A'
    },
    {
        text: 'B',
        value: 'B'
    },
    {
        text: 'C',
        value: 'C'
    },
    {
        text: 'D',
        value: 'D'
    },
])

const select = ref([
    {
        text: '正确',
        value: 'true'
    },
    {
        text: '错误',
        value: 'false'
    },
])

const qn = ref<qn>(old_qn)

const qn_store = useQnStore()
const { upd_qn } = qn_store
</script>

<style lang="scss" scoped></style>