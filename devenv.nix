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
  devcontainer.enable = true;

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
        name = "quiz_db";
        schema = ./db/quiz.sql;
      }
    ];
  };

  services.redis.enable = true;

  enterShell = ''
    java --version
    node -v
    pnpm -v
  '';
}
