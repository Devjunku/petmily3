package com.ssafy.petmily.db.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.petmily.db.entity.animal.AnimalJoin;
import com.ssafy.petmily.db.entity.community.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.List;

@Repository
public class BoardRepositorySupport {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    QBoard qBoard =QBoard.board;
    QBoardJoin qBoardJoin = QBoardJoin.boardJoin;
    QReplyJoin qReplyJoin = QReplyJoin.replyJoin;
    QBoardFile qBoardFile = QBoardFile.boardFile;


    public BoardJoin findBoardByNo(Long no) {
        BoardJoin boardJoins = jpaQueryFactory.select(qBoardJoin).from(qBoardJoin)
                .where(qBoardJoin.no.eq(no)).fetchOne();
        return boardJoins;
    }

    public long getMaxNo() {
        long num =0;
        if(jpaQueryFactory.select(qBoard.no.max()).from(qBoard).fetchOne() ==null){
            num =0;
        }else{
            num =jpaQueryFactory.select(qBoard.no.max()).from(qBoard).fetchOne();
        }

        return num;
    }

    public List<BoardJoin> getBoardList(String agencycode, long userno, String word){
        // 전체 목록 보여줌
        if(word == null || word.equals("") || word.equals(" ")){
            if(userno > 0) // 자신이 쓴글 조회
                return jpaQueryFactory.select(qBoardJoin).from(qBoardJoin)
                        .where(qBoardJoin.userno.eq(userno)).fetch();
            else if(agencycode != null){ // 기관회원 코드가 비어있지 않을 때
                return jpaQueryFactory.select(qBoardJoin).from(qBoardJoin)
                        .where(qBoardJoin.agencycode.eq(agencycode)).fetch();
            }
            else
            return jpaQueryFactory.select(qBoardJoin).from(qBoardJoin).fetch();
        }
        // 제목 + 내용으로 검색
        else if(userno > 0) // 자신이 쓴글 조회
            return jpaQueryFactory.select(qBoardJoin).from(qBoardJoin)
                    .where(qBoardJoin.title.like("%" + word + "%").or(qBoardJoin.contents.like("%" + word + "%")), qBoardJoin.userno.eq(userno)).fetch();
        else if(agencycode != null) // 기관회원 코드가 비어있지 않을 때
            return jpaQueryFactory.select(qBoardJoin).from(qBoardJoin)
                .where(qBoardJoin.title.like("%" + word + "%").or(qBoardJoin.contents.like("%" + word + "%")) , qBoardJoin.agencycode.eq(agencycode)).fetch();
        else
            return jpaQueryFactory.select(qBoardJoin).from(qBoardJoin)
                    .where(qBoardJoin.title.like("%" + word + "%").or(qBoardJoin.contents.like("%" + word + "%"))).fetch();

    }

    @Transactional
    public void deleteFile(long no){
        jpaQueryFactory.delete(qBoardFile).where(qBoardFile.boardno.eq(no)).execute();
    }
}
