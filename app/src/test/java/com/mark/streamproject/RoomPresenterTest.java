package com.mark.streamproject;

import com.mark.streamproject.data.Room;
import com.mark.streamproject.data.User;
import com.mark.streamproject.room.RoomContract;
import com.mark.streamproject.room.RoomPresenter;
import com.mark.streamproject.util.UserManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;

import java.util.Observable;

public class RoomPresenterTest {
    @Mock
    Room mRoom;
    User mUser;
    @Mock
    RoomContract.View mView;
    RoomPresenter mPresenter;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mUser = new User();
        mRoom = new Room();
        mRoom.setTitle("安安你好");
        mRoom.setImage("https://img.youtube.com/vi/g7UbHffdYo0/hqdefault.jpg");
        mRoom.setTag("娛樂");
        mRoom.setPublishTime(1566572166758L);
        mRoom.setLike(0);
        mRoom.setDislike(0);
        mRoom.setStreamerId("I5LDgYWYe3kuq3soOe0B");
        mRoom.setStreamerImage("https://cdn.discordapp.com/emojis/545990332249014274.png?v=1");
        mRoom.setStreamerName("Mock Man");
        mRoom.setWatchId("g7UbHffdYo0");
        mPresenter = new RoomPresenter(mView);
        mPresenter.setRoomData(mRoom);
        UserManager.getInstance().setUser(mUser);
    }

    @Test
    public void add2LikeList_isCorrect() {
        mPresenter.add2LikeList();
        assertEquals(UserManager.getInstance().getUser().getLikeList().get(0), "g7UbHffdYo0");
    }

    @Test
    public void add2LikeList_isIncorrect() {
        mPresenter.add2LikeList();
        Assert.assertNotEquals(UserManager.getInstance().getUser().getLikeList().get(0), "MbRGvYskZs8");
    }

    @Test
    public void add2DislikeList_isCorrect() {
        mPresenter.add2DislikeList();
        assertEquals(UserManager.getInstance().getUser().getDislikeList().get(0), "g7UbHffdYo0");
    }

    @Test
    public void add2DislikeList_isIncorrect() {
        mPresenter.add2DislikeList();
        Assert.assertNotEquals(UserManager.getInstance().getUser().getDislikeList().get(0), "MbRGvYskZs8");
    }

    @Test
    public void add2FollowList_isCorrect() {
        mPresenter.add2FollowList();
        assertEquals(UserManager.getInstance().getUser().getFollowList().get(0), "I5LDgYWYe3kuq3soOe0B");
    }

    @Test
    public void add2FollowList_isIncorrect() {
        mPresenter.add2FollowList();
        Assert.assertNotEquals(UserManager.getInstance().getUser().getFollowList().get(0), "TeEkVUitxeG2RqVA7BMW");
    }


}
