package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;


import java.util.List;

public interface FavoriteDao {
    public Favorite findByRidAndUid(int rid, int uid);

    int findCountByRid(int rid);

    void add(int rid, int uid);

    void remove(int rid, int uid);

    List<Favorite> findAllByUid(int uid, int start, int pageSize);

    int findCountByUid(int uid);
}
