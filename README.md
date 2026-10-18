# Nahi 

## 准备工作

## 构建发行版本

发行版本用于正常使用, 不含 TabooLib 本体。

```
./gradlew build
```

## 构建开发版本

开发版本包含 TabooLib 本体, 用于开发者使用, 但不可运行。

```
./gradlew taboolibBuildApi -PDeleteCode
```

> 参数 -PDeleteCode 表示移除所有逻辑代码以减少体积。

## TabooLib 自动更新

GitHub Workflow 每天检查官方 Maven 仓库中最近发布的 `x.y.z-commitHash` 构件
（例如 `6.3.0-0e3a911`），跳过 `test`、`dev` 等版本，不再只跟随 GitHub Release。
有更新时，构建通过后自动提交并推送到默认分支（当前为 `main`），不再创建升级 PR。
构建失败或版本未变时不推送；也可在 Actions 中手动运行 `Update TabooLib`。
