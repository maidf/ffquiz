-- 用户表
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account VARCHAR(32) UNIQUE COMMENT '账号',
    name VARCHAR(32) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(32) COMMENT '邮箱',
    role ENUM('STUDENT', 'TEACHER') NOT NULL COMMENT '角色',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户注册时间'
) COMMENT = '用户表';

-- 题库表
CREATE TABLE question_bank (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '题库名',
    subject VARCHAR(100) NOT NULL COMMENT '学科分类（如数学、编程）',
    creator_id BIGINT COMMENT '创建者ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '题库创建时间',
    FOREIGN KEY (creator_id) REFERENCES user (id)
) COMMENT = '题库表';

-- 题目表
CREATE TABLE question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bank_id BIGINT COMMENT '所属题库ID',
    type ENUM(
        'SINGLE_CHOICE',
        'MULTIPLE_CHOICE',
        'FILL_BLANK',
        'TRUE_FALSE'
    ) NOT NULL COMMENT '题型',
    content TEXT NOT NULL COMMENT '题目',
    options TEXT COMMENT '选项',
    answer TEXT NOT NULL COMMENT '题目答案',
    ana TEXT COMMENT '题目解析',
    diff ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL COMMENT '题目难度',
    creator_id BIGINT COMMENT '创建者ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '题目创建时间',
    FOREIGN KEY (bank_id) REFERENCES question_bank (id),
    FOREIGN KEY (creator_id) REFERENCES user (id)
) COMMENT = '题目表';

-- 试卷表
CREATE TABLE paper (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '试卷名称',
    time_limit INT COMMENT '考试时间限制(分钟,0表示不限时)',
    diff ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL COMMENT '试卷难度',
    total_score INT NOT NULL DEFAULT 0 COMMENT '试卷总分',
    creator_id BIGINT COMMENT '创建者ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '试卷创建时间',
    FOREIGN KEY (creator_id) REFERENCES user (id)
) COMMENT = '试卷表';

-- 试卷题目关联表
CREATE TABLE paper_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paper_id BIGINT COMMENT '试卷ID',
    question_id BIGINT COMMENT '题目ID',
    score INT COMMENT '题目分数',
    FOREIGN KEY (paper_id) REFERENCES paper (id),
    FOREIGN KEY (question_id) REFERENCES question (id),
    UNIQUE (paper_id, question_id)
) COMMENT = '试卷与题目关联表';

-- 考试记录表
CREATE TABLE exam (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    paper_id BIGINT NOT NULL COMMENT '试卷ID',
    score INT NOT NULL COMMENT '得分',
    start_time DATETIME COMMENT '开始答题时间',
    end_time DATETIME COMMENT '结束答题时间',
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (paper_id) REFERENCES paper (id)
) COMMENT = '考试记录表';

-- 答题记录表
CREATE TABLE ans_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    exam_id BIGINT COMMENT '考试记录ID',
    user_answer VARCHAR(255) COMMENT '用户提交的答案',
    start_time DATETIME COMMENT '开始答题时间',
    end_time DATETIME COMMENT '结束答题时间',
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (question_id) REFERENCES question (id),
    FOREIGN KEY (exam_id) REFERENCES exam (id)
) COMMENT = '答题记录表';

-- 错题记录表
CREATE TABLE mistake (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    question_id BIGINT NOT NULL COMMENT '题目ID',
    user_answer VARCHAR(255) NOT NULL COMMENT '用户提交的答案',
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (question_id) REFERENCES question (id)
) COMMENT = '用户错题本表';

CREATE VIEW retry_stats AS
SELECT
    q.id,
    q.content,
    qb.subject as sub,
    q.diff,
    /* 重刷率 = 有重刷行为的用户数 / 总用户数 */
    COUNT(DISTINCT retry_users.user_id) * 1.0 / NULLIF(COUNT(DISTINCT ar.user_id), 0) AS retry_rate,
    /* 正确率 = 正确回答次数 / 总回答次数 */
    SUM(
        CASE
            WHEN ar.user_answer = q.answer THEN 1
            ELSE 0
        END
    ) * 1.0 / NULLIF(COUNT(ar.id), 0) AS avg_acc
FROM
    question q
    JOIN question_bank qb ON q.bank_id = qb.id
    JOIN ans_record ar ON q.id = ar.question_id
    LEFT JOIN (
        SELECT question_id, user_id
        FROM ans_record
        GROUP BY
            question_id,
            user_id
        HAVING
            COUNT(*) > 1
    ) AS retry_users ON q.id = retry_users.question_id
    AND ar.user_id = retry_users.user_id
GROUP BY
    q.id,
    q.content,
    qb.subject,
    q.diff;

CREATE VIEW bank_qn_num_view AS
SELECT
    qb.id AS id,
    qb.name AS name,
    qb.subject AS sub,
    COUNT(DISTINCT q.id) AS qn_nums, -- 题库题目总数
    COUNT(ar.id) AS use_nums, -- 题目总使用次数（所有答题记录）
    SUM(
        CASE
            WHEN ar.user_answer != q.answer THEN 1
            ELSE 0
        END
    ) AS err_nums -- 题目总错误次数
FROM
    question_bank qb
    LEFT JOIN question q ON qb.id = q.bank_id -- 关联题库和题目
    LEFT JOIN ans_record ar ON q.id = ar.question_id -- 关联题目和答题记录
GROUP BY -- 按题库分组统计
    qb.id,
    qb.name,
    qb.subject;