package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.request.BookingTableRequest;
import com.ktpm1.restaurant.dtos.request.BookingTableUpdateRequest;
import com.ktpm1.restaurant.dtos.response.AllTableStatus;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.BookingTable;
import com.ktpm1.restaurant.models.Catalog;
import com.ktpm1.restaurant.models.Table;
import com.ktpm1.restaurant.models.User;
import com.ktpm1.restaurant.repositories.BookingTableRepository;
import com.ktpm1.restaurant.repositories.CatalogRepository;
import com.ktpm1.restaurant.repositories.TableRepository;
import com.ktpm1.restaurant.repositories.UserRepository;
import com.ktpm1.restaurant.services.BookingTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class BookingTableServiceImpl implements BookingTableService {
    @Autowired
    private BookingTableRepository bookingTableRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public ResponseMessage createBookingTable(String username, BookingTableRequest bookingTableRequest) {
        User user = userRepository.findByUsername(username);
        List<Long> tableIds = bookingTableRequest.getTableIds();

        for (Long tableId : tableIds) {
            if (!isTableAvailable(tableId, bookingTableRequest.getStartTime(), bookingTableRequest.getAdditionalTime())) {
                throw new RuntimeException("Table is not available at the selected time.");
            } else {
                Table table = tableRepository.findById(tableId).orElse(null);
                BookingTable bookingTable = new BookingTable();
                bookingTable.setUser(user);
                bookingTable.setTable(table);
                bookingTable.setBookingTime(LocalDateTime.now());
                bookingTable.setStartTime(bookingTableRequest.getStartTime());
                LocalDateTime endTime = LocalDateTime.of(bookingTableRequest.getStartTime().toLocalDate(),
                        LocalTime.of(bookingTableRequest.getStartTime().getHour() + 2 + bookingTableRequest.getAdditionalTime(),
                                bookingTableRequest.getStartTime().getMinute()));
                bookingTable.setEndTime(endTime);

                if (!isTableAvailable(tableId, bookingTableRequest.getStartTime(), bookingTableRequest.getAdditionalTime())) {
                    throw new RuntimeException("Table is not available at the selected time.");
                }

                bookingTableRepository.save(bookingTable);
            }
        }

        return new ResponseMessage(200, "Đặt bàn thành công.");
    }

    @Override
    public boolean isTableAvailable(Long tableId, LocalDateTime startTime, int additionalTime) {
        LocalDateTime endTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(startTime.getHour() + 2 + additionalTime, startTime.getMinute()));
        Table table = tableRepository.findById(tableId).orElse(null);
        boolean isOverlapping = bookingTableRepository.existsByTableAndStartTimeBetween(table, startTime, endTime)
                || bookingTableRepository.existsByTableAndEndTimeAfterAndStartTimeBefore(table, startTime, endTime)
                || bookingTableRepository.existsByTableAndStartTimeBeforeAndEndTimeAfter(table, startTime, endTime);
        return !isOverlapping;
    }

    public boolean isTableAvailable(Long tableId, LocalDateTime startTime, int additionalTime, Long bookingIdToExclude) {
        LocalDateTime endTime = startTime.plusHours(2 + additionalTime);
        Table table = tableRepository.findById(tableId).orElse(null);

        boolean isOverlapping = bookingTableRepository.existsByTableAndStartTimeBetweenAndIdNot(
                table, startTime, endTime, bookingIdToExclude)
                || bookingTableRepository.existsByTableAndEndTimeAfterAndStartTimeBeforeAndIdNot(
                table, startTime, endTime, bookingIdToExclude)
                || bookingTableRepository.existsByTableAndStartTimeBeforeAndEndTimeAfterAndIdNot(
                table, startTime, endTime, bookingIdToExclude);

        return !isOverlapping;
    }

    @Override
    public List<BookingTable> getBookingsByDate(LocalDateTime date) {
        return bookingTableRepository.findAllByStartTimeBetween(
                date.withHour(0).withMinute(0),
                date.withHour(23).withMinute(59)
        );
    }

    @Override
    public BookingTable updateBooking(String username, BookingTableUpdateRequest bookingTableRequest, Long bookingId) {
        // Tìm kiếm user
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }

        // Lấy thông tin booking cũ
        BookingTable bookingTable = bookingTableRepository.findById(bookingId).orElse(null);
        if (bookingTable == null) {
            throw new RuntimeException("Booking not found.");
        }

        // Kiểm tra xem user có quyền chỉnh sửa booking này không
        if (!bookingTable.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to update this booking.");
        }

        // Lấy bàn hiện tại và bàn mới (nếu cần thay đổi)
        Table currentTable = bookingTable.getTable();
        Table newTable = tableRepository.findById(bookingTableRequest.getTableId()).orElse(null);

        if (newTable == null) {
            throw new RuntimeException("Table not found.");
        }

        // Kiểm tra xem bàn có thay đổi hoặc thời gian bắt đầu có thay đổi
        boolean isTableChanged = !currentTable.getId().equals(newTable.getId());
        boolean isStartTimeChanged = !bookingTable.getStartTime().isEqual(bookingTableRequest.getStartTime());

        // Nếu bàn hoặc thời gian bắt đầu thay đổi, cần kiểm tra tính khả dụng
        if (isTableChanged || isStartTimeChanged) {
            if (!isTableAvailable(newTable.getId(), bookingTableRequest.getStartTime(), bookingTableRequest.getAdditionalTime(), bookingId)) {
                throw new RuntimeException("Table is not available at the selected time.");
            }
        }

        // Cập nhật thông tin booking
        bookingTable.setUser(user);
        bookingTable.setTable(newTable);
        bookingTable.setStartTime(bookingTableRequest.getStartTime());

        // Tính toán và cập nhật `endTime`
        LocalDateTime endTime = bookingTableRequest.getStartTime().plusHours(2);
        if (bookingTableRequest.getAdditionalTime() > 0) {
            endTime = endTime.plusHours(bookingTableRequest.getAdditionalTime());
       }
        bookingTable.setEndTime(endTime);

        return bookingTableRepository.save(bookingTable);
    }

    @Override
    public List<Table> showStatusTableByAvailable(LocalDateTime startTime, int additionalTime, Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId).orElse(null);
        if (catalog == null) {
            // Xử lý trường hợp catalog không tìm thấy
            return null; // Hoặc có thể throw một exception tùy theo yêu cầu của hệ thống
        }

        LocalDateTime endTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(startTime.getHour() + 2 + additionalTime, startTime.getMinute()));
        List<Table> unavailableTables = tableRepository.findByCatalogAndAvailable(catalog, false);
        List<Table> availableTables = tableRepository.findByCatalogAndAvailable(catalog, true);
        List<Table> newlyUnavailableTables = new ArrayList<>();

        for (Table table : availableTables) {
            boolean isOverlapping = bookingTableRepository.existsByTableAndStartTimeBetween(table, startTime, endTime)
                    || bookingTableRepository.existsByTableAndEndTimeAfterAndStartTimeBefore(table, startTime, endTime)
                    || bookingTableRepository.existsByTableAndStartTimeBeforeAndEndTimeAfter(table, startTime, endTime);
            if (isOverlapping) {
                newlyUnavailableTables.add(table);
            }
        }

        availableTables.removeAll(newlyUnavailableTables);
        unavailableTables.addAll(newlyUnavailableTables);

        for (Table table : newlyUnavailableTables) {
            table.setAvailable(false);
        }

        List<Table> allTables = new ArrayList<>();
        allTables.addAll(availableTables);
        allTables.addAll(unavailableTables);

        allTables.sort(Comparator.comparing(Table::getId));
        return allTables;
    }

    @Override
    public ResponseMessage cancelBooking(String username, Long bookingId) {
        BookingTable bookingTable = bookingTableRepository.findById(bookingId).orElse(null);
        if (!bookingTable.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to cancel this booking.");
        }
        LocalDateTime bookingTime = bookingTable.getBookingTime();
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isBefore(bookingTime.plusMinutes(15))) {
            bookingTableRepository.delete(bookingTable);
        } else {
//            throw new RuntimeException("You can only cancel within 15 minutes after booking.");
            return new ResponseMessage(400, "Bạn chỉ có thể hủy đặt bàn trong vòng 15 phút sau khi đặt.");
        }
        return new ResponseMessage(200, "Hủy đặt bàn thành công.");
    }
}
