{
  pkgs,
  inputs,
  ...
}:
let
  unpkgs = import inputs.unpkgs { system = pkgs.stdenv.system; };
in
{
  cachix.enable = false;

  # https://devenv.sh/basics/
  env.GREET = "devenv";

  # https://devenv.sh/packages/
  # packages = [ pkgs.git ];

  languages.java = {
    enable = true;
    jdk.package = unpkgs.jdk;
    maven.enable = true;
  };

  languages.javascript = {
    enable = true;
    package = unpkgs.nodejs-slim;
    pnpm = {
      enable = true;
      package = unpkgs.pnpm;
    };
  };

  services.mysql = {
    enable = true;
    package = unpkgs.mysql80;
    ensureUsers = [
      {
        name = "quiz";
        password = "123";
        ensurePermissions = {
          "quiz.*" = "ALL PRIVILEGES";
        };
      }
    ];
    initialDatabases = [
      {
        name = "quiz";
        schema = ./db/quiz.sql;
      }
    ];
  };

  services.redis.enable = true;

  # 项目启动流程
  # processes = {
  #   backend = {
  #     exec = "cd javaquiz && mvn spring-boot:run";
  #     process-compose.depends_on = {
  #       redis.condition = "process_running";
  #       mysql.condition = "process_running";
  #     };
  #   };
  #   frontend.exec = "cd uniquiz && pnpm dev:h5";
  # };

  # https://devenv.sh/languages/
  # languages.rust.enable = true;

  # https://devenv.sh/processes/
  # processes.cargo-watch.exec = "cargo-watch";

  # https://devenv.sh/services/
  # services.postgres.enable = true;

  # https://devenv.sh/scripts/
  scripts.hello.exec = ''
    echo hello from $GREET
  '';

  enterShell = ''
    hello
    java --version
    node -v
    pnpm -v
  '';
}
