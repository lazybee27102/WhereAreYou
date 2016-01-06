package study.project.whereareyou.SqlHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import study.project.whereareyou.OOP.User;
import study.project.whereareyou.OtherUsefullClass.Message;

/**
 * Created by Administrator on 22/11/2015.
 */
public class MySqlOpenHelper {

    SqlHelper helper;

    public MySqlOpenHelper(Context context) {
        if(helper==null)
            helper = new SqlHelper(context);
    }

    //////USERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
    public void insertUser(User user)
    {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlHelper.key_user_Id,user.getId());
        values.put(SqlHelper.key_user_Email,user.getEmail());
        values.put(SqlHelper.key_user_Name,user.getUserName());
        values.put(SqlHelper.key_user_FirstName,user.getFirstName());
        values.put(SqlHelper.key_user_LastName,user.getLastName());
        values.put(SqlHelper.key_user_BirthDate,user.getBirthDate());
        values.put(SqlHelper.key_user_LastLocation,user.getLastLocation());
        database.insert(SqlHelper.table_user,null,values);
        database.close();
    }

    public User getUserByName(String name)
    {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SqlHelper.table_user + " WHERE " + SqlHelper.key_user_Name + "=?", new String[]{name});
        if(cursor.moveToFirst())
        {
            User user = new User(cursor.getString(0),
                    cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
            return user;
        }
        return null;
    }

    public User getUserById(int id)
    {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+SqlHelper.table_user+" WHERE "+ SqlHelper.key_user_Id+"=?",new String[]{String.valueOf(id)});
        if(cursor.moveToFirst())
        {
            User user = new User(cursor.getString(0),
                    cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
            return user;
        }
        return null;
    }

    public String getLastUserId()
    {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT "+SqlHelper.key_user_Id+" FROM "+SqlHelper.table_user,null);
        if(cursor.moveToFirst())
        {
            cursor.moveToLast();
            return cursor.getString(0);
        }
        return null;
    }

    /////END USERRRRRRRRRRRRRRRRRRRRRRRRRR













    static class SqlHelper extends SQLiteOpenHelper{
        private Context context;

        private static final String DATABASE_NAME = "ManagerUser";
        private static final int SQLITE_VERSION = 3;
        //TABE|LE USER
        private static final String table_user = "tb_user";
        private static final String key_user_Id = "Id";
        private static final String key_user_Email = "Email";
        private static final String key_user_Name = "Name";
        private static final String key_user_FirstName = "FirstName";
        private static final String key_user_LastName = "LastName";
        private static final String key_user_BirthDate = "BirthDate";
        private static final String key_user_LastLocation = "LastLocation";

        /*//TABLE CONVERSATION
        private static final String table_conversation = "tb_conversation";
        private static final String key_conversation_Id = "Id";
        private static final String key_conversation_Name = "Name";
        private static final String key_conversation_Type = "Type";
        private static final String key_conversation_BeginningDate = "BeginningDate";


        //TABLE_USERLIST
        private static final String table_userList = "tb_userlist";
        private static final String key_userList_ConversationId = "ConversationId";
        private static final String key_userList_UserId = "UserId";

        //MSGLIST
        private static final String table_msgList = "tb_msglist";
        private static final String key_msg_ConversationId = "ConversationId";
        private static final String key_msg_MessageId = "MessageId";
        private static final String key_msg_Time = "Time";
        private static final String key_msg_UserId = "UserId";
        private static final String key_msg_MsgString = "MsgString";
        private static final String key_msg_IsSent = "IsSent";

        //TABLE_TYPE
        private static final String table_type = "tb_Type";
        private static final String key_type_TypeId = "TypeId";
        private static final String key_type_TypeName = "TypeName";

        */
        public SqlHelper(Context context) {
            super(context, DATABASE_NAME, null, SQLITE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try
            {

                String CREATE_TABLE_USER = "CREATE TABLE " + table_user + "("+key_user_Id+ " TEXT PRIMARY KEY," +key_user_Email+" TEXT," +key_user_Name+" TEXT," +key_user_FirstName+" TEXT,"  + key_user_LastName +" TEXT,"+ key_user_BirthDate +" TEXT,"+key_user_LastLocation +" TEXT);";

                /*String CREATE_TABLE_TYPE = "CREATE TABLE " + table_type + "("+ key_type_TypeId + " TEXT PRIMARY KEY," + key_type_TypeName +" text);";
                String CREATE_TABLE_CONVERSATION = "CREATE TABLE " + table_conversation + "("+ key_conversation_Id + " TEXT PRIMARY KEY," + key_conversation_Name +" TEXT," + key_conversation_Type +" INTEGER," + key_conversation_BeginningDate +" TEXT,FOREIGN KEY(" + key_conversation_Type +") REFERENCES "+table_type+"("+ key_type_TypeId +"));";
                String CREATE_TABLE_USERLIST = "CREATE TABLE " + table_userList + "("+ key_userList_ConversationId + " TEXT," + key_userList_UserId +" TEXT,"+"PRIMARY KEY(" +key_userList_ConversationId+","+key_userList_UserId+"),FOREIGN KEY(" + key_userList_ConversationId +") REFERENCES "+table_conversation+"("+ key_conversation_Id +"));";
                String CREATE_TABLE_MSGLIST = "CREATE TABLE " + table_msgList + "("+ key_msg_ConversationId + " TEXT PRIMARY KEY," + key_msg_MessageId +" TEXT," + key_msg_Time +" TEXT," + key_msg_UserId +" TEXT," + key_msg_MsgString +" TEXT,"+ key_msg_IsSent +" INTEGER,FOREIGN KEY("+ key_msg_ConversationId +") REFERENCES "+table_conversation+"("+ key_conversation_Id +"));";*/

                db.execSQL(CREATE_TABLE_USER);
                /*db.execSQL(CREATE_TABLE_TYPE);
                db.execSQL(CREATE_TABLE_CONVERSATION);
                db.execSQL(CREATE_TABLE_USERLIST);
                db.execSQL(CREATE_TABLE_MSGLIST);*/
            }catch (SQLiteException e)
            {
                Message.printMessage(context,e+"");
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try
            {
               /* db.execSQL("DROP TABLE IF EXISTS "+ table_msgList);
                db.execSQL("DROP TABLE IF EXISTS "+ table_userList);
                db.execSQL("DROP TABLE IF EXISTS "+ table_conversation);
                db.execSQL("DROP TABLE IF EXISTS "+ table_type);*/
                db.execSQL("DROP TABLE IF EXISTS " + table_user);
                onCreate(db);
            }catch (SQLiteException e)
            {
                Message.printMessage(context, e + "");
            }

        }
    }

}
