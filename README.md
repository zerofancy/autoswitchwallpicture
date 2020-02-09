# autoswitchwallpicture
自动切换壁纸的程序，就是简单的遍历目录然后调用系统命令设置壁纸。

代码写的很随意，在deepin15.11、Ubuntu18.04、Windows10测试没问题。

## 工作原理
程序启动后读配置文件`data.json`和`config.json`，如果有`data.json`数据就取出一条并调用系统设置壁纸的命令。

如果没有数据，就遍历`config.json`指定的文件夹，并将结果打乱顺序存放到`data.json`。

## 使用
### 依赖
jre1.7+

> 如果java是通过`sdkman`安装的，添加桌面启动器会找不到`jre`，此时可以修改启动脚本`utowallpicture.sh`，指定jre所在位置。

### 配置
编辑`config.json`，它只有两条配置：

- picpath 指定包含图片的文件夹所在位置（绝对路径）
- setcommand 当前桌面环境下设置壁纸的命令，用$pic代指当前选中的图片的路径。

#### Ubuntu（gnome3）示例
```
{
    "picpath":"/media/zero/diskF/wallpictures/pics",
    "setcommand":"gsettings set org.gnome.desktop.background picture-uri '$pic'"
}
```

#### deepin示例
```
{
    "picpath":"/media/zero/diskF/wallpictures/pics",
    "setcommand":"gsettings set com.deepin.wrap.gnome.desktop.background picture-uri '$pic'"
}
```

#### Windows
由于没找到Windows下比较好用的设置壁纸的命令，`setWallPicture.exe`是我用C#写的一个简单的设置壁纸的程序（源码丢了），信不过可以自己写。

```
{
    "picpath":"F:\\wallpictures\\pics",
    "setcommand":"setWallPicture.exe \"$pic\""
}
```

### 添加启动器
当然也只能自己手动添加了。下面的代码保存成`修改壁纸.desktop`，路径改为自己保存的位置。然后放到桌面上就大功告成了。

```
[Desktop Entry]
Type=Application
Icon=applications-photography
Name=切换壁纸
Exec=/media/zero/diskF/wallpictures/ubuntu/autowallpicture.sh
Terminal=false
Hidden=false
Categories=Graphics; Settings; Utility
```

如果有开机启动的需要可以拷到`~/.config/autostart`下。
