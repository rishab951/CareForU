package com.cbitss.careforu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rishab on 12/29/2016.
 */

public class FirstAidCard {

    int id;
    String title,content;
    String imageId;

    FirstAidCard(int id,String title,String content,String imageId)
    {
        this.title = title;
        this.id=id;
        this.content = content;
        this.imageId = imageId;
    }


}
