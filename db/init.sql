-- 用户表
CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户id',
    user_name VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名（登录用，唯一）',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱（用于找回密码，唯一）',
    role ENUM('student', 'teacher') NOT NULL COMMENT '用户角色：student（学生）/ teacher（教师）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '用户注册时间',
    status BOOLEAN DEFAULT TRUE COMMENT '状态：0-封禁，1-正常'
) ENGINE=InnoDB COMMENT='用户信息表';

-- 题库表
CREATE TABLE question_bank (
    bank_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '题库唯一标识',
    name VARCHAR(100) NOT NULL COMMENT '题库名称（如“高中数学题库”）',
    subject VARCHAR(50) NOT NULL COMMENT '学科分类（如数学、编程）',
    creator_id INT NOT NULL COMMENT '创建者ID（关联 user.user_id）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '题库创建时间',
    FOREIGN KEY (creator_id) REFERENCES user(user_id)
) ENGINE=InnoDB COMMENT='题库信息表';

-- 题目表
CREATE TABLE question (
    question_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '题目唯一标识',
    bank_id INT NOT NULL COMMENT '所属题库ID（关联 question_bank.bank_id）',
    type ENUM('single_choice', 'multiple_choice', 'fill_blank', 'true_false') NOT NULL COMMENT '题型：single_choice（单选）/ multiple_choice（多选）/ fill_blank（填空）/ true_false（判断）',
    content TEXT NOT NULL COMMENT '题干内容（支持富文本）',
    answer JSON NOT NULL COMMENT '题目答案（JSON格式，根据题型动态定义）',
    explanation TEXT COMMENT '题目解析（可为空）',
    knowledge_tags JSON COMMENT '知识点标签（JSON数组，如["函数","几何"]）',
    difficulty ENUM('easy', 'medium', 'hard') NOT NULL COMMENT '题目难度：easy（简单）/ medium（中等）/ hard（困难）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '题目创建时间',
    FOREIGN KEY (bank_id) REFERENCES question_bank(bank_id)
) ENGINE=InnoDB COMMENT='题目信息表';

-- 试卷表
CREATE TABLE paper (
    paper_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '试卷唯一标识',
    name VARCHAR(100) NOT NULL COMMENT '试卷名称',
    creator_id INT NOT NULL COMMENT '创建者ID（关联 user.user_id）',
    total_score INT DEFAULT 100 COMMENT '试卷总分',
    time_limit INT COMMENT '考试时间限制（分钟，0表示不限时）',
    difficulty ENUM('easy', 'medium', 'hard') NOT NULL COMMENT '题目难度：easy（简单）/ medium（中等）/ hard（困难）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '试卷创建时间',
    FOREIGN KEY (creator_id) REFERENCES user(user_id)
) ENGINE=InnoDB COMMENT='试卷信息表';

-- 试卷题目关联表
CREATE TABLE paper_question (
    paper_id INT COMMENT '试卷ID（关联 paper.paper_id）',
    question_id INT COMMENT '题目ID（关联 question.question_id）',
    `order` INT NOT NULL COMMENT '题目在试卷中的显示顺序',
    PRIMARY KEY (paper_id, question_id),
    FOREIGN KEY (paper_id) REFERENCES paper(paper_id),
    FOREIGN KEY (question_id) REFERENCES question(question_id)
) ENGINE=InnoDB COMMENT='试卷与题目关联表';

-- 刷题记录表
CREATE TABLE practice_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '刷题记录唯一标识',
    user_id INT NOT NULL COMMENT '用户ID（关联 user.user_id）',
    paper_id INT COMMENT '试卷ID（关联 paper.paper_id，随机刷题时可为空）',
    score INT NOT NULL COMMENT '得分',
    start_time DATETIME NOT NULL COMMENT '开始答题时间',
    end_time DATETIME NOT NULL COMMENT '结束答题时间',
    details JSON NOT NULL COMMENT '答题详情（JSON数组，记录每题的答案和正误）',
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (paper_id) REFERENCES paper(paper_id)
) ENGINE=InnoDB COMMENT='用户刷题记录表';

-- 错题本表
CREATE TABLE mistake_book (
    mistake_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '错题记录唯一标识',
    user_id INT NOT NULL COMMENT '用户ID（关联 user.user_id）',
    question_id INT NOT NULL COMMENT '题目ID（关联 question.question_id）',
    wrong_answer VARCHAR(255) NOT NULL COMMENT '用户提交的错误答案',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '错题记录时间',
    is_resolved BOOLEAN DEFAULT FALSE COMMENT '是否已掌握：0-未解决，1-已掌握',
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (question_id) REFERENCES question(question_id)
) ENGINE=InnoDB COMMENT='用户错题本表';



-- 用户表索引
CREATE INDEX idx_user_role ON user(role);

-- 题库表索引
CREATE INDEX idx_bank_subject ON question_bank(subject);
CREATE INDEX idx_bank_creator ON question_bank(creator_id);

-- 题目表索引
CREATE INDEX idx_question_type ON question(type);
CREATE INDEX idx_question_difficulty ON question(difficulty);
CREATE INDEX idx_question_bank ON question(bank_id);

-- 刷题记录表索引
CREATE INDEX idx_history_user ON practice_history(user_id);
CREATE INDEX idx_history_paper ON practice_history(paper_id);

-- 错题本表索引
CREATE INDEX idx_mistake_user ON mistake_book(user_id);
CREATE INDEX idx_mistake_question ON mistake_book(question_id);