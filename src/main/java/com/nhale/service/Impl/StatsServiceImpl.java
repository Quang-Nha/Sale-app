/*
 *
 */
package com.nhale.service.Impl;

import com.nhale.repository.StatsRepository;
import com.nhale.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author LeNha
 */
@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    /**
     * @return list các mảng Object[],
     * 1 mảng gồm các phần tử id của danh mục, tên danh mục và phần tử chứa số lượng sản phẩm có trong danh mục đó
     */
    @Override
    public List<Object[]> cateStats() {
        return statsRepository.cateStats();
    }

    /**
     * thống kê doanh thu các sản phẩm theo khoảng thời gian
     * lấy chọn lọc theo tên sản phẩm có chứa từ tìm kiếm truyền vào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     *
     * @param kw       từ tìm kiếm
     * @param fromDate từ ngày
     * @param toDate   đến ngày
     * @return list các mảng Object, mỗi mảng chứa sản phẩm và doanh thu của nó
     */
    @Override
    public List<Object[]> productStats(String kw, Date fromDate, Date toDate) {
        return this.statsRepository.productStats(kw, fromDate, toDate);
    }

    /**
     * thống kê doanh thu các sản phẩm theo từng mốc thời gian theo tháng, năm
     * lấy chọn lọc theo tên sản phẩm phải giống từ tìm kiếm truyền vào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     *
     * @param kw       từ tìm kiếm
     * @param fromDate từ ngày
     * @param toDate   đến ngày
     * @return list các mảng Object, mỗi mảng chứa tháng, năm và doanh thu của tháng năm đó
     */
    @Override
    public List<Object[]> productMonthStats(String kw, Date fromDate, Date toDate) {
        return this.statsRepository.productMonthStats(kw, fromDate, toDate);
    }

    /**
     * thống kê doanh thu các sản phẩm theo từng mốc thời gian theo ngày, tháng, năm
     * lấy chọn lọc theo tên sản phẩm phải giống từ tìm kiếm truyền vào
     * và khoảng thời gian đặt hàng các sản phẩm đó
     *
     * @param kw       từ tìm kiếm
     * @param fromDate từ ngày
     * @param toDate   đến ngày
     * @return list các các mảng Object, mỗi mảng chứa ngày, tháng, năm và doanh thu của tháng năm đó
     */
    @Override
    public List<Object[]> productDayStats(String kw, Date fromDate, Date toDate) {
        return this.statsRepository.productDayStats(kw, fromDate, toDate);
    }
}
