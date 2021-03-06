package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Info;
import com.example.springboot.entity.Station;
import com.example.springboot.entity.Train;
import com.example.springboot.entity.Train_station;
import com.example.springboot.mapper.StationMapper;
import com.example.springboot.mapper.TrainMapper;
import com.example.springboot.service.StationService;
import com.example.springboot.service.TrainService;
import com.example.springboot.service.TrainStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train> implements TrainService {


    @Autowired
    private TrainStationService trainStationService;

    @Autowired
    private TrainService trainService;


    @Override
    public List<Train> queryByTS(List<Info> infoList, boolean onlyHigh){

        System.out.println("size:"+infoList.size());
        List<BigInteger>list = new ArrayList<>();

        for(Info info : infoList) list.add(info.getTrainId());

        QueryWrapper<Train> queryWrapper = new QueryWrapper<>();
        List<Train> list1 = new ArrayList<>();

        if(onlyHigh) {
            queryWrapper.in("train_id", list).
                    like("train_type", "动车").or().
                    like("train_type", "高速列车").or().
                    like("train_type", "城际动车");

            list1 = baseMapper.selectList(queryWrapper);
        }else {
            list1 = baseMapper.selectBatchIds(list);
        }
        /*list1.sort((o1, o2) -> {
            if(o1.getTrainDepartDate().before(o2.getTrainDepartDate())){
                return -1;
            }else if(o1.getTrainDepartDate().after(o2.getTrainDepartDate())){
                return 1;
            }else {
                if(o1.getTrainDepartTime().before(o2.getTrainDepartTime())){
                    return -1;
                }else if(o1.getTrainDepartTime().after(o2.getTrainDepartTime())){
                    return 1;
                }else return 0;
            }
        });*/
        return list1;
    }

    @Override
    public BigInteger queryByNum(String trainNum){

        QueryWrapper<Train> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("train_number",trainNum);
        List<Train> trainList = trainService.list(queryWrapper);
        if(trainList.size()==0) return null;
        else return trainList.get(0).getTrainId();

    }

}
