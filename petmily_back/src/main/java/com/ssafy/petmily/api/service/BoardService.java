package com.ssafy.petmily.api.service;

import com.ssafy.petmily.api.request.ComuRegisterPostReq;
import com.ssafy.petmily.api.request.FileBoardPostReq;
import com.ssafy.petmily.db.entity.community.Board;
import com.ssafy.petmily.db.entity.community.BoardFile;
import com.ssafy.petmily.db.entity.community.BoardJoin;

import java.util.List;

public interface BoardService {


    Board createBoard(ComuRegisterPostReq comuRegisterPostReq);

    void deleteBoard(Long no);

    BoardFile fileUpload(String filedir, String extension);

    BoardJoin boarddetail(Long no);

    List<Board> getBoadList();
}
