# Super Schedule
 5046 project
 
 多人协作的方案就根据这个[博客](https://www.liaoxuefeng.com/wiki/896043488029600/900375748016320)说的来做。
 
 首先远程库会有master，dev分支。我们开发就在dev分支上进行，每个人clone到本地后创建自己的feature分支在上面进行开发，开发的差不多了就merge进dev分支然后pull到远程库上。然后由于我们开始的状态下没什么东西，只要保证dev分支的下的app能打开，页面都能显示就行，新做好了什么东西都可以丢上去。然后自己的feature分支就算没完全开发好最好也pull到远程，让别人都有个参考。
 而master分支就是开发到比较完整的时候会更新。

 screen的设计我还是照搬了之前的文档，如果要修改的话负责界面设计这部分的人自己去改成新的样式
 
 因为网络功能都得连到google，所以要用这些功能都必须要翻墙才行。
如果用的代理的话，android 虚拟机和主机是在同一个局域网里的，在Android模拟器里面，搜setting->Network&internet->internet->长按 android Wifi(未连接状态)->modify 里面设置代理就行了。ip填主机的局域网ip，端口填代理端口就行

# *

## ViewModel 
 
在编写viewmodel 层的时候请使用Switchmap方法更新LiveData

具体说明如下：

[livedata](https://developer.android.google.cn/topic/libraries/architecture/livedata#transform_livedata) 

[Switchmap](https://developer.android.google.cn/reference/androidx/lifecycle/Transformations#switchMap(android.arch.lifecycle.LiveData%3CX%3E,%20android.arch.core.util.Function%3CX,%20android.arch.lifecycle.LiveData%3CY%3E%3E))



Example：

    class MyViewModel extends ViewModel {
        private final PostalCodeRepository repository;
        private final MutableLiveData<String> addressInput = new MutableLiveData();
        public final LiveData<String> postalCode =
                Transformations.switchMap(addressInput, (address) -> {
                    return repository.getPostCode(address);
                 });

      public MyViewModel(PostalCodeRepository repository) {
          this.repository = repository
      }

      private void setInput(String address) {
          addressInput.setValue(address);
      }
    }

 
# 数据

## 数据模型

1. Event （日程）

       public String uid; //自动生成的uid
       public String eventName;  //日程名

       public String time; //time->"2022-05-11-01-11"

       public String ownerCalendar; //每个Event从属于某一Calendar，ownerCalendar记录此 Calendar的uid
       public Boolean enableAlarm; //该Calendar是否需要提醒（该功能可以砍）

       public String location;  //记录该日程发生的地点（可以与Map screen 集成，也可以砍了）

2. Calendar（日程表）（为简化设计，群组将与Calendar一一对应，Calendar兼任群组的功能）
    
    
       public String uid; //自动生成的uid
       public String calendarName;  //Calendar的名称
       public String ownerUser;  //每个Calendar有一个Owner用户，该项目记录其uid

       public Boolean isShared; //用于记录该Calendar是否为共享日程表


3. User（用户信息）
       
       public String uid; //Firebase Auth为用户分配固定的uid
       
       public String name;  //用户名
       
       public String password; //密码
       
       public String email; //邮箱
       
       public String phone;  //手机号
       
       FirebaseAuth
       .getInstance()
       .getCurrentUser()
       {.getEmail()|
        .getPhotoUrl()|//头像，暂未实现
        .getDisplayName()|
        .getUid()|
        .getPhoneNumber()|
        .}

4. CalendarMember （记录User与Calendar的对应关系）（存储Calendar的成员名单/某成员的所属Calendar表）
       
       public String calendarUid; // Calendar uid
       
       public String userUid; // 成员的 uid
       
       public int userAuthLv;  //该 Calendar中该成员的权限等级

                               //0->viewer

                               //1->editor
                               
                               //2->owner(remote)
                               
                               //3->owner(local)


## 存储方案
由于项目要求使用room为recycleview提供数据(Livedata)，但事实上Firebase realtime database也具备本地数据缓存的功能。

因此仅当Calendar为非共享表的时候，使用ROOM存储相关数据。同时使用Workmanager，定时将其推送到云端（反之则不自动将其下载到本地/或者设置一个按钮，直接用云端数据覆盖本地数据,以避免同步数据的麻烦）

在Room中可以考虑仅存储Calendar，Event。 过滤时仅需要将当前用户的uid与Calendaruid比对

同时为满足使用Workmanager的要求，定时将ROOM中数据推送到云端（反之则不自动将其下载到本地/或者设置一个按钮，直接用云端数据覆盖本地数据,以避免同步数据的麻烦）

提供的数据类型与ROOM一致，Livedata/CompletableFuture。 

True-> 仅通过Firebase Cloud database存储/仅读取来自于Cloud database的数据

False->写入在本地的ROOM数据库，在特定时间通过Workmanager 存储于Clouddatabase/从ROOM中读取
（对应Android RecyclerView with CardView to display data from the Room dbs (using LiveData)的要求）

## Realtime database 存储模型

Json树

    {
      "backup":{
       "users":{...},
       "calendars":{...},
       "events":{...},
       "calendarmembers":{...}
       
      }
      "users":{
       
       "uid1":{USER_ENTITIY},
       
       "uid2":{...},
       ...
       
      },
      "calendars":{
       
       "uid1":{CALENDAR}
       
       },
       "uid2":{...},
       ...
      
      },
      "events":{
       
        "eventuid1":{ EVENT_ENTITIY }
        "eventuid2":{ EVENT_ENTITIY }
        "eventuid3":{ EVENT_ENTITIY }
      
      }
      "calendarmembers":{
       "by_user":{
       
         "user_uid1":{
          "calendaruid1":{CALENDARMEMBER_ENTITIY},
          "calendaruid2":{CALENDARMEMBER_ENTITIY,
          "calendaruid3":{CALENDARMEMBER_ENTITIY},
          ...
         },
         "user_uid2":{
          "calendarruid1":{CALENDARMEMBER_ENTITIY},
          "calendaruid2":{CALENDARMEMBER_ENTITIY},
          "calendaruid3":{CALENDARMEMBER_ENTITIY},
          ...
         },
         "user_uid3":{...},
         ...
   
   
       }
        
        
        }
      "by_calendar":{
         
          "calendar_uid1":{
           "calendarmemberuid1":{CALENDARMEMBER_ENTITIY},
           "calendarmemberuid2":{CALENDARMEMBER_ENTITIY},
           "calendarmemberuid3":{CALENDARMEMBER_ENTITIY},
           ...
         },
         "calendar_uid2":{
           "calendarmemberuid1":{CALENDARMEMBER_ENTITIY},
           "calendarmemberuid2":{CALENDARMEMBER_ENTITIY},
           "calendarmemberuid3":{CALENDARMEMBER_ENTITIY},
           ...
         },
         "calendar_uid3":{...},
         ...
         
        }
     
    }
   
## 提供的查询方法



# Task Allocation

## Zixuan Huang

    App key functions and screen funcitons designing
    
    Task planning and allocating
    
    Data structure designing
    
    Workmanager development
    
    Firebase Auth development
    
    Firebase Realtime database development
    
    Room development
    
    Sign in screen

    Sign up screen

## Zhenxiao Yu

    The UI Design and Screen Mockups 

    Design the UI of the app

    Design the interaction between the screens

    Design the logic architeture of the app

    UI Controller development

    Navigation Drawer

    App testing

    Respository layer

    Retrofit

## Xianhan Cui
    Screen functions designing and report writing

    Main screen


    Event editor screen

    Events Alarming function

    Group Screen
    
    group manager functions

## Yukun Ding

    Key components assigning,a few screens designing 

    Map screen &function
    
    Report Screen

    Report Screen

    Calendar&Events screen

    shared calendar function
    
    ViewModel-Livedata



# Screens

## 登录页(1) //1号页面


      1 （邮箱）用户名-EditView

      2  密码-EditView

      3  登陆-Button->(3) //代表按下后转跳(3)号页面

      4  注册-Button->(2)

 (通过LiveData来实现：当用户登录之后，该用户的界面中的登录按钮自动转换成已经登录，并且自动显示该用户的头像)


## 注册页（用户信息页）(2)

      1 用户名*-EditView

      2. 用户头像-ImageButton ->[文件选择]

      3. 邮箱*-EditView

      4. 手机号-EditView

      5. 密码*-EditView

      6. 确认密码*-EditView

      7 注册（确认）-Button->(3)

      8. 返回-Button->(3)

## 主页（Dashboard）(3)

       1. Fragment1

             1.1. 时间，日期--CalendarView

             1.2. 天气--CardView

                    1.2.1 地点-TextView

                    1.2.2 温度-TextView

                    1.2.3. 天气-TextView，ImageView 

       2. Fragment2

            2.1. 日历/日程--Button->(4)

            2.2. 地理位置--Button->(5)

       3. [导航边栏]

## 日历页(4)

       1. Fragment1

          1.1 选择当前展示的日历表--Spinner(spinner是组件名称，这个组件可以用来选择个人表，或群组表。也就是说，下拉按钮，出线选框，里面有所有的用户的日程，有的被共享了，有的没被共享）（未登陆不可用）

          1.2 日期显示、日历（各日期框为可选中控件/当日有日程则附加标记）-CalendarView

          1.3. 新增日程表-按钮-> 弹出窗口->

               1.3.1. 日程表表名-EditView

               1.3.2. 共享选项- ToggleButton(个人，共享）

               1.3.3. 确认-Button

               1.3.4. 取消-Button

               （使用LiveData,使得当用户新增日程时，该用户的日程表发生变化，添加上新的日程）

           1.4.  群组设置（若该表为共享表）->(8)

       2. Fragment2

          2.1. 显示当前日期存在的日程-RecyclerView

               [Elements 1] 日程 CardView（可选中）-->[单击]（6）

                            1.日程名-TextView

                            2.日程时间，日期-TextView

                            3. 是否提醒-ToggleButton

                [Elements n]

          2.2. 删除-Button

          2.3. 添加日程-Button->(6)

       3.[导航边栏]


## 地图页面(5)

       1、 经纬度显示-TextView

       2、 地图显示-?

       3、 位置记录（时间，经纬度记录）-Fragment

           3.1. 时间-TextView、

           3.2. 经纬度-TextView

           3.3. 地点名称-TextView

       4、 共享位置-Button->弹窗选择共享群组

       5、 查看（查看某群组内已共享的位置）-Button->弹窗查看某群组位置

       6、 [导航边栏]

## 日程编辑页(6)

       1. 日程名称-EditView

       2. 时间-DatePicker，TimePicker

       3. 是否提醒-ToggleButton

       4. 所属群组: -Spinner
          [个人日程表，群组A，群组B...]

       5. 地点-?

          5.1. 地点选择--Button->进入地图页(5)->选择地点-->返回位置

          5.2. 地点手动输入--EditView

       6. 取消-Button

       7. 确认-Button

       （使用LIveData，当按下确认按钮时，新编辑的日程被同步到该用户的日程，当按下取消按钮时，用户退出编辑）

## 导航边栏(Navigation Drawer)(7)

      1. 用户头像--ImageButton ->[文件选择]

      2. 用户名（已登录状态）--TextView->（2）用户信息页  / 

      登陆(未登录状态)--Button>（1）

      3.用文字显示当前地理位置--TextView->（5）

      4.列出n个最常查看的日历表-ListView

        4.1. 个人日程表a   -Button->(4)

        4.2. 来自群组a   -Button->(4)

        4. ...

      5.切换用户-Button->(1)

      6.登出-Button


## Calendar信息页(8)

    1. 选择当前显示的Calendar-Spinner（显示 Calendar名称+[个人/共享]）（Owner 可编辑名称，放置编辑按钮）

    2. 你的权限等级--TextView（Owner ：可修改成员权限，踢出成员

                            /Editor ：可发布编辑删除日程

                            /Viewer：可查看日程）

    3. 生成加入链接-Button->弹出窗口（显示当前Calendar的UID）（）

    4. 成员表-RecyclerView

       4.1. 成员A-CardView

             4.1.1.成员名称-TextView

             4.1.2.权限等级-Spinner(Owner可编辑)

             4.1.3.移除--Button（Owner 可选）

       4.2. 成员B

       4.3. ...

    5. 返回--Button->(4)
    6. 加入--Button-->弹窗（填入Calendar的UID以加入）
    7. 删除--Button-->删除当前的Calendar
    8. 新建--Button-->弹窗（新建Calendar）

    9. 共享位置-ToggleButton（可删）

    10. 查看成员位置--Button->(5)（可删）

# Functions
========================================================

(1) 服务器端通过Firebase Authentication认证用户功能

(2) 服务器端通过Firebase Database存储用户信息，存储日程信息功能

(3) 用户注册功能

(4) 用户登陆功能（未登录也可以享受日历，天气等有限功能）

(5) 通过WorkManager 在后台将本地数据同步到云端

(6) 基本日历表功能，日期显示


(7) 查看，添加，修改，删除日程功能

(8) 日程内通过地图页选择地点功能

(9) 日程定时提醒功能-AlarmManager

(10) 共享日程表功能（允许多人对服务端存储的共享日程表添加，查看，修改，删除）

(11) 共享日程表-用户分组功能（划分权限组）


(12) 地图、定位功能

(13) 共享位置功能

(14) 查看某群组内其他人共享的位置


(15) 根据定位显示当地天气


