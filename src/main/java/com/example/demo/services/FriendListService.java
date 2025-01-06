package com.example.demo.services;

import com.example.demo.controllers.ListingController;
import com.example.demo.entities.FriendList;
import com.example.demo.entities.FriendRequestStatus;
import com.example.demo.entities.UserDetails;
import com.example.demo.repositories.FriendListRepository;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendListService {

    @Autowired
    private FriendListRepository friendListRepository;

    @Autowired
    private KeycloakUserService keycloakUserService;

    private static final Logger logger = LoggerFactory.getLogger(FriendListService.class);


    public List<FriendList> getFriendListOfUser(String userId){
        List<FriendList> friendsList=friendListRepository.findByUserId(userId);
        logger.debug("FriendListService.getFriendListOfUser params {}, return {}",userId,friendsList);
        if(friendsList.size()==0){
            return null;
        }else{
            return friendsList;
        }
    }

    public boolean friendRequest(String senderId,String receiverId){
        UserRepresentation sender=keycloakUserService.getUserById(senderId);
        logger.debug("FriendListService.friendRequest() called params senderId {} ,receiverId {}",senderId,receiverId );
        if(!friendListRepository.isFriendRequestOrFriendshipExists(senderId,receiverId)){
            friendListRepository.save(new FriendList(senderId,receiverId,FriendRequestStatus.STATUS_PENDING_SENDER));
            friendListRepository.save(new FriendList(receiverId,senderId,FriendRequestStatus.STATUS_PENDING_RECEIVER));
            logger.debug("FriendListService.friendRequest() new friendship created");
            return true;
        }
        logger.debug("FriendListService.friendRequest() friendship exists in db");
        return false;
    }

    public boolean acceptFriendRequest(String requestAccepter,String receiverId){
        logger.debug("FriendListService.acceptFriendRequest( {} , {} )",requestAccepter,receiverId);
        if(friendListRepository.isFriendRequestOrFriendshipExists(requestAccepter,receiverId)
        && verifyReceiver(requestAccepter,receiverId)){
            List<FriendList> friendLists=friendListRepository.findFriendshipRows(requestAccepter,receiverId);
            try{
                FriendList first=friendLists.get(0);
                first.setStatus(FriendRequestStatus.STATUS_ACCEPTED);
                FriendList second=friendLists.get(1);
                second.setStatus(FriendRequestStatus.STATUS_ACCEPTED);
                friendListRepository.save(first);
                friendListRepository.save(second);
                logger.debug("FriendListService.acceptFriendRequest first {} second {}",first,second);
                return true;
            }catch (RuntimeException e){
                logger.warn("Error ",e);
                return false;
            }
        }
        return false;
    }


    /* @Param requestId the one who makes the accept request must be the one who received the friend request
    *  @Param senderId the one who sent the friend request
    * */
    private boolean verifyReceiver(String requestId,String senderId){
        Optional<FriendList> request=friendListRepository.findByUserIdAndFriendIdAndStatus(requestId,senderId,FriendRequestStatus.STATUS_PENDING_RECEIVER);
        logger.debug("FriendListService.verifyReceiver() requestId {}, senderId {} request.isPresent {}",requestId,senderId,request.isPresent());
        if(request.isPresent())
            return true;
        return false;
    }

    }
