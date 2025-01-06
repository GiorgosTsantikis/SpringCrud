package com.example.demo.repositories;

import com.example.demo.entities.FriendList;
import com.example.demo.entities.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendListRepository  extends JpaRepository<FriendList,Long> {
    public List<FriendList> findByUserId(String userId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM FriendList f " +
            "WHERE (f.userId = :userId AND f.friendId = :friendId) " +
            "OR (f.userId = :friendId AND f.friendId = :userId)")
    boolean isFriendRequestOrFriendshipExists(@Param("userId") String userId,
                                              @Param("friendId") String friendId);

    @Query(value = "SELECT * FROM friend_list f " +
            "WHERE (f.user_id = :userId AND f.friend_id = :friendId) " +
            "OR (f.user_id = :friendId AND f.friend_id = :userId)", nativeQuery = true)
    List<FriendList> findFriendshipRows(@Param("userId") String userId,
                                        @Param("friendId") String friendId);

    Optional<FriendList> findByUserIdAndFriendIdAndStatus(String userId, String friendId, FriendRequestStatus status);

}
