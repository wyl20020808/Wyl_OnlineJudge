package com.wyl.backend.classes.judge;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 用于提交做题信息给judge0的实体类
 */
@Data
@Builder
@ToString
@AllArgsConstructor
public class Submission {
    //    程序的源代码
    private String source_code;
    //    语言的id，可通过{server_url}/languages来查询
    private Integer language_id;
    //    程序的输入
    private String stdin;
    //    程序的预期输出
    private String expected_output;
    //    cpu运行时间限制，根据服务器配置，应当设置为不高于5
    private float cpu_time_limit;
    //    超时运行的额外时间，默认为1s（用于计算程序的实际运行时间）
    private float cpu_extra_time;
    //    防止程序睡眠的重要参数，根据服务器配置，应当设置为不高于10
    private float wall_time_limit;
    //    限制程序的地址空间，根据服务器配置，应当设置为不高于128000
    private float memory_limit;
    //    限制进程的堆栈，根据服务器配置，应当设置为不高于64000
    private int stack_limit;
    //    程序可以创建的最大进程和/或线程数，根据服务器配置，应当设置为不高于60
    private int max_processes_and_or_threads;
    //    为true表示:将标准错误重定向为标准输出。true就完事了
    private Boolean redirect_stderr_to_stdout;
    //    限制程序创建或修改的文件大小，根据服务器配置，应当设置为不高于1024
    private int max_file_size;
    //    程序的命令行
    private String command_line_arguments;
    //    编译器标识
    private String compiler_options;
    //    应与源代码一起提供的其他文件。此字符串的值应表示包含其他文件的 的内容。此属性对于多文件程序是必需的。.zip
    private String additional_files;


    public Submission() {
        super();
        this.cpu_time_limit = 5;
        this.cpu_extra_time = 1;
        this.wall_time_limit = 10;
        this.memory_limit = 128000;
        this.stack_limit = 64000;
        this.max_processes_and_or_threads = 60;
        this.redirect_stderr_to_stdout = true;
        this.max_file_size = 1024;
    }

    public Submission(String source_code, Integer language_id) {
        this();
        this.source_code = source_code;
        this.language_id = language_id;
    }

    public Submission(String source_code, Integer language_id, String stdin, String expected_output, float cpu_time_limit, float memory_limit) {
        this();
        this.source_code = source_code;
        this.language_id = language_id;
        this.stdin = stdin;
        this.expected_output = expected_output;
        this.cpu_time_limit = cpu_time_limit;
        this.memory_limit = memory_limit;
    }
}
