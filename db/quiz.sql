-- 用户表
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account VARCHAR(32) UNIQUE COMMENT '账号',
    name VARCHAR(32) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(32) COMMENT '邮箱',
    role ENUM('STUDENT', 'TEACHER') NOT NULL COMMENT '角色',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户注册时间'
) COMMENT = '用户表';

-- 题库表
CREATE TABLE question_bank (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '题库名',
    subject VARCHAR(100) NOT NULL COMMENT '学科分类（如数学、编程）',
    creator_id INT COMMENT '创建者ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '题库创建时间',
    FOREIGN KEY (creator_id) REFERENCES user (id)
) COMMENT = '题库表';

-- 题目表
CREATE TABLE question (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bank_id INT NOT NULL COMMENT '所属题库ID',
    type ENUM(
        'SINGLE_CHOICE',
        'MULTIPLE_CHOICE',
        'FILL_BLANK',
        'TRUE_FALSE'
    ) NOT NULL COMMENT '题型',
    content TEXT NOT NULL COMMENT '题目',
    answer JSON NOT NULL COMMENT '题目选项和答案 {options,answer}/{answer}',
    explanation TEXT COMMENT '题目解析',
    difficulty ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL COMMENT '题目难度',
    creator_id INT COMMENT '创建者ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '题目创建时间',
    FOREIGN KEY (bank_id) REFERENCES question_bank (id),
    FOREIGN KEY (creator_id) REFERENCES user (id)
) COMMENT = '题目表';

-- 试卷表
CREATE TABLE paper (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '试卷名称',
    time_limit INT COMMENT '考试时间限制(分钟,0表示不限时)',
    difficulty ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL COMMENT '试卷难度',
    creator_id INT COMMENT '创建者ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '试卷创建时间',
    FOREIGN KEY (creator_id) REFERENCES user (id)
) COMMENT = '试卷表';

-- 试卷题目关联表
CREATE TABLE paper_question (
    paper_id INT COMMENT '试卷ID',
    question_id INT COMMENT '题目ID',
    score INT COMMENT '题目分数',
    PRIMARY KEY (paper_id, question_id),
    FOREIGN KEY (paper_id) REFERENCES paper (id),
    FOREIGN KEY (question_id) REFERENCES question (id)
) COMMENT = '试卷与题目关联表';

-- 考试记录表
CREATE TABLE exam (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL COMMENT '用户ID',
    paper_id INT NOT NULL COMMENT '试卷ID',
    score INT NOT NULL COMMENT '得分',
    start_time DATETIME COMMENT '开始答题时间',
    end_time DATETIME COMMENT '结束答题时间',
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (paper_id) REFERENCES paper (id)
) COMMENT = '考试记录表';

-- 答题记录表
CREATE TABLE answer_record (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL COMMENT '用户ID',
    question_id INT NOT NULL COMMENT '题目ID',
    exam_id INT COMMENT '考试记录ID',
    user_answer VARCHAR(255) NOT NULL COMMENT '用户提交的答案',
    start_time DATETIME COMMENT '开始答题时间',
    end_time DATETIME COMMENT '结束答题时间',
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (question_id) REFERENCES question (id),
    FOREIGN KEY (exam_id) REFERENCES exam (id)
) COMMENT = '答题记录表';

-- 错题记录表
CREATE TABLE mistake (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL COMMENT '用户ID',
    question_id INT NOT NULL COMMENT '题目ID',
    user_answer VARCHAR(255) NOT NULL COMMENT '用户提交的答案',
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (question_id) REFERENCES question (id)
) COMMENT = '用户错题本表';