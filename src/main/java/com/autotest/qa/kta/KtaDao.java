package com.autotest.qa.kta;



import com.autotest.qa.annotations.UseDataBase;
import com.autotest.qa.common.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 操作测试数据库
 * 根据业务需求修改
 */
@Component
@Slf4j
public class KtaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 删除
     * @return
     */
    @UseDataBase("loanDb")
    public Boolean deleteTempProduct(){
        String sql = "DELETE FROM loan_user_product WHERE product_cd = 'KT';";
        jdbcTemplate.update(sql);
        return true;
    }

    /**
     * 刷新数据库业务时间
     * @param bizTime
     * @return
     */
    @UseDataBase("loanDb")
    public Boolean updateBizTime(Long bizTime){
        String sql = "UPDATE loan_system_config SET config_value = '%s' WHERE config_key = 'biz_time';";
        sql = String.format(sql,bizTime);
        jdbcTemplate.update(sql);
        return true;
    }


    /**
     * 根据手机号码查询用户信息
     * @return
     */
    @UseDataBase("userDb")
    public Map<String,Object>  queryUserInfo(String mobileNo){

        String querySQL = "SELECT * FROM user_info WHERE mobile_no = '%s';";
        querySQL = String.format(querySQL,mobileNo);
        log.info("执行SQL==>{}",querySQL);
        try {
            return jdbcTemplate.queryForMap(querySQL);
        }catch (Exception e){
            ContextHolder.interruptTest();
            log.error("执行SQL失败,信息为==>{}",e.toString());
            return null;
        }
    }

    /**
     * 查询授信申请风控流水号
     * @param creditApplyNo
     * @return
     */
    @UseDataBase("aggDb")
    public Map<String,Object>  queryCreditRiskNo(String creditApplyNo){
        String querySQL = "SELECT * FROM agg_risk_record WHERE credit_apply_no = '%s';";
        querySQL = String.format(querySQL,creditApplyNo);
        log.info("执行SQL==>{}",querySQL);
        try {
            return jdbcTemplate.queryForMap(querySQL);
        }catch (Exception e){
            ContextHolder.interruptTest();
            log.error("执行SQL失败,信息为==>{}",e.toString());
            return null;
        }
    }

    /**
     * 查询授用户借款状态信息
     * @param userId
     * @return
     */
    @UseDataBase("aggDb")
    public Map<String,Object>  queryUserLoanInfo(String userId){
        String querySQL = "SELECT * FROM agg_user_loan_info WHERE user_id ='%s';";
        querySQL = String.format(querySQL,userId);
        log.info("执行SQL==>{}",querySQL);
        try {
            return jdbcTemplate.queryForMap(querySQL);
        }catch (Exception e){
            ContextHolder.interruptTest();
            log.error("执行SQL失败,信息为==>{}",e.toString());
            return null;
        }
    }

    /**
     * 查询借款申请风控流水号
     * @param loanApplyNo
     * @return
     */
    @UseDataBase("aggDb")
    public Map<String,Object>  queryLoanRiskNo(String loanApplyNo){
        String querySQL = "SELECT * FROM agg_risk_record WHERE loan_apply_no = '%s';";
        querySQL = String.format(querySQL,loanApplyNo);
        log.info("执行SQL==>{}",querySQL);
        try {
            return jdbcTemplate.queryForMap(querySQL);
        }catch (Exception e){
            ContextHolder.interruptTest();
            log.error("执行SQL失败,信息为==>{}",e.toString());
            return null;
        }
    }

    /**
     * 查询用户VA
     */
    @UseDataBase("loanDb")
    public Map<String,Object> queryUserVa(String userId){
        String querySQL="SELECT * FROM loan_user_virtual_account WHERE user_id = '%s';";
        querySQL = String.format(querySQL,userId);
        log.info("执行SQL==>{}",querySQL);
        try {
            return   this.jdbcTemplate.queryForMap(querySQL);
        }catch (Exception e){
            ContextHolder.interruptTest();
            log.error("执行SQL失败,信息为==>{}",e.toString());
            return null;
        }
    }

    /**
     * 查询某笔账单第几期的数据
     * @param billNo
     * @param termNo
     * @return
     */
    @UseDataBase("loanDb")
    public Map<String,Object> queryBillDueDay(String billNo,int termNo){
        String querySQL="SELECT * FROM loan_term WHERE bill_no = '%s' and term_no= '%d';";
        querySQL = String.format(querySQL,billNo,termNo);
        log.info("执行SQL==>{}",querySQL);
        try {
            return   this.jdbcTemplate.queryForMap(querySQL);
        }catch (Exception e){
            ContextHolder.interruptTest();
            log.error("执行SQL失败,信息为==>{}",e.toString());
            return null;
        }
    }



    /**
     * 查询还款记录
     * @param serialNo
     * @return
     */
    @UseDataBase("loanDb")
    public Map<String,Object> queryRepay(String serialNo){
        String querySQL = "SELECT * FROM loan_repay_record WHERE serial_no = '%s';";
        querySQL = String.format(querySQL,serialNo);
        log.info("执行SQL==>{}",querySQL);
        try {
            return   this.jdbcTemplate.queryForMap(querySQL);
        }catch (Exception e){
            ContextHolder.interruptTest();
            log.error("执行SQL失败,信息为==>{}",e.toString());
            return null;
        }
    }


}
