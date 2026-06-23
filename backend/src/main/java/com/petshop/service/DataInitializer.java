package com.petshop.service;

import com.petshop.entity.*;
import com.petshop.repository.*;
import com.petshop.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数据初始化 - 启动时插入演示数据
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final VideoRepository videoRepository;

    @Override
    public void run(String... args) {
        initAdmin();
        initCategoriesAndProducts();
        initStores();
        initVideos();
    }

    private void initAdmin() {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(PasswordUtil.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setNickname("管理员");
            admin.setEmail("admin@petshop.com");
            admin.setPhone("13800000000");
            admin.setMemberLevel(3);
            userRepository.save(admin);
            log.info("初始化管理员账号: admin / admin123");

            User user = new User();
            user.setUsername("user");
            user.setPassword(PasswordUtil.encode("user123"));
            user.setRole("USER");
            user.setNickname("萌宠达人");
            user.setEmail("user@petshop.com");
            user.setPhone("13800000001");
            user.setMemberLevel(1);
            userRepository.save(user);
            log.info("初始化测试用户账号: user / user123");
        }
    }

    private void initCategoriesAndProducts() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    createCategory("猫粮", "🐱"),
                    createCategory("狗粮", "🐶"),
                    createCategory("零食", "🦴"),
                    createCategory("玩具", "🎾"),
                    createCategory("清洁护理", "🧴"),
                    createCategory("窝具", "🏠"),
                    createCategory("保健品", "💊"),
                    createCategory("外出用品", "🎒")
            );
            categoryRepository.saveAll(categories);
            log.info("初始化分类: {} 个", categories.size());
        }

        if (productRepository.count() == 0) {
            List<Product> products = List.of(
                    createProduct("皇家猫粮 室内成猫粮 2kg", 1L, new BigDecimal("159.00"), 100,
                            "专为室内猫咪设计，低脂配方，减少毛球产生，适合1-10岁室内成猫。", "/uploads/products/cat-food-royal.jpg", "ACCESSORY", 1L),
                    createProduct("渴望六种鱼无谷猫粮 1.8kg", 1L, new BigDecimal("289.00"), 50,
                            "高蛋白无谷配方，新鲜鱼肉为主要原料，营养均衡美味。", "/uploads/products/cat-food-fish.jpg", "ACCESSORY", 1L),
                    createProduct("冠能中型犬成犬粮 3kg", 2L, new BigDecimal("189.00"), 80,
                            "适合中型犬成犬，含优质蛋白质和益生元，增强免疫力。", "/uploads/products/dog-food-adult.jpg", "ACCESSORY", 1L),
                    createProduct("比瑞吉全价幼犬粮 2kg", 2L, new BigDecimal("139.00"), 60,
                            "专为幼犬设计，富含DHA促进大脑发育，易消化吸收。", "/uploads/products/dog-food-puppy.jpg", "ACCESSORY", 1L),
                    createProduct("冻干鸡肉粒宠物零食 100g", 3L, new BigDecimal("39.90"), 200,
                            "纯鸡肉冻干，无添加，高蛋白低脂肪，猫狗均可食用。", "/uploads/products/pet-treats-chicken.jpg", "ACCESSORY", 1L),
                    createProduct("猫条零食 混合口味 20支装", 3L, new BigDecimal("25.90"), 300,
                            "液体零食，适口性极佳，多种口味可选，可作奖励。", "/uploads/products/cat-treats-stick.jpg", "ACCESSORY", 1L),
                    createProduct("逗猫棒羽毛玩具", 4L, new BigDecimal("15.90"), 150,
                            "仿真羽毛逗猫棒，铃铛设计，激发猫咪捕猎天性。", "/uploads/products/cat-toy-feather.jpg", "ACCESSORY", 2L),
                    createProduct("狗狗飞盘 环保TPU材质", 4L, new BigDecimal("29.90"), 90,
                            "柔软耐咬飞盘，适合户外互动训练，增进与狗狗感情。", "/uploads/products/dog-toy-frisbee.jpg", "ACCESSORY", 2L),
                    createProduct("宠物干洗泡沫 免洗清洁", 5L, new BigDecimal("35.00"), 120,
                            "免洗清洁泡沫，快速去污除味，温和不刺激，适合不爱洗澡的宠物。", "/uploads/products/pet-grooming-foam.jpg", "ACCESSORY", 2L),
                    createProduct("半封闭猫窝 四季通用", 6L, new BigDecimal("69.00"), 35,
                            "半封闭设计给猫咪安全感，柔软短绒面料，可拆洗。", "/uploads/products/cat-bed-house.jpg", "ACCESSORY", 1L),
                    createProduct("大型犬棉窝 加厚保暖", 6L, new BigDecimal("119.00"), 25,
                            "加厚棉填充，适合大型犬，底部防滑，四季可用。", "/uploads/products/dog-bed-cushion.jpg", "ACCESSORY", 1L),
                    createProduct("素力高金装无谷猫粮 2kg", 1L, new BigDecimal("259.00"), 45,
                            "美国进口无谷高蛋白猫粮，含42种营养素。", "/uploads/products/cat-food-solidgold.jpg", "ACCESSORY", 1L),
                    createProduct("伟嘉妙鲜包湿粮 85g×12", 1L, new BigDecimal("35.90"), 150,
                            "湿粮包组合装，多种口味，鲜嫩肉块汤汁。", "/uploads/products/cat-food-whiskas.jpg", "ACCESSORY", 1L),
                    createProduct("美士全阶猫粮 3kg", 1L, new BigDecimal("168.00"), 55,
                            "天然成分，含真鸡肉，无人工色素防腐剂。", "/uploads/products/cat-food-nutro.jpg", "ACCESSORY", 1L),
                    createProduct("爱肯拿农场盛宴犬粮 2kg", 2L, new BigDecimal("320.00"), 30,
                            "加拿大进口，多种鲜肉配方，富含Omega脂肪酸。", "/uploads/products/dog-food-acana.jpg", "ACCESSORY", 1L),
                    createProduct("皇家幼犬奶糕 1kg", 2L, new BigDecimal("85.00"), 70,
                            "专为离乳期幼犬设计，易泡软易消化。", "/uploads/products/dog-food-puppy-royal.jpg", "ACCESSORY", 1L),
                    createProduct("伯纳天纯低敏犬粮 5kg", 2L, new BigDecimal("299.00"), 40,
                            "低敏配方，单一蛋白来源，无谷无麸质。", "/uploads/products/dog-food-pure.jpg", "ACCESSORY", 1L),
                    createProduct("洁齿骨磨牙棒 中型犬装", 3L, new BigDecimal("49.90"), 180,
                            "清洁牙齿减少牙垢，薄荷口味清新口气。", "/uploads/products/pet-treats-dental.jpg", "ACCESSORY", 1L),
                    createProduct("三文鱼冻干猫零食 50g", 3L, new BigDecimal("45.00"), 120,
                            "新鲜三文鱼冻干，保留营养，美毛亮肤。", "/uploads/products/pet-treats-salmon.jpg", "ACCESSORY", 1L),
                    createProduct("鸭肉绕薯条宠物零食", 3L, new BigDecimal("32.00"), 90,
                            "鸭肉缠绕薯条，低脂健康零食，训练奖励佳品。", "/uploads/products/pet-treats-duck.jpg", "ACCESSORY", 1L),
                    createProduct("猫隧道互动玩具 三通道", 4L, new BigDecimal("59.90"), 60,
                            "三通道隧道设计，带逗猫洞口，可折叠收纳。", "/uploads/products/cat-toy-tunnel.jpg", "ACCESSORY", 2L),
                    createProduct("狗狗拔河绳结玩具", 4L, new BigDecimal("25.90"), 110,
                            "棉绳编织绳结，适合中大型犬拔河互动。", "/uploads/products/dog-toy-rope.jpg", "ACCESSORY", 2L),
                    createProduct("发光弹力球宠物玩具", 4L, new BigDecimal("19.90"), 200,
                            "LED夜光弹力球，落地弹跳发光，猫狗都爱玩。", "/uploads/products/pet-toy-ball.jpg", "ACCESSORY", 2L),
                    createProduct("宠物香波沐浴露 500ml", 5L, new BigDecimal("45.00"), 100,
                            "温和无刺激配方，深层清洁毛发，含燕麦精华。", "/uploads/products/pet-grooming-shampoo.jpg", "ACCESSORY", 2L),
                    createProduct("半封闭猫砂盆 防带砂", 5L, new BigDecimal("79.00"), 50,
                            "半封闭设计防异味扩散，防带砂走廊。", "/uploads/products/pet-litter-box.jpg", "ACCESSORY", 2L),
                    createProduct("宠物指甲剪修剪套装", 5L, new BigDecimal("22.90"), 140,
                            "锋利不锈钢刀头，防滑手柄，带安全限位器。", "/uploads/products/pet-grooming-nail.jpg", "ACCESSORY", 2L),
                    createProduct("多层猫爬架 大型猫树", 6L, new BigDecimal("399.00"), 15,
                            "多层平台+猫窝+吊床，天然剑麻柱磨爪。", "/uploads/products/cat-tree-tower.jpg", "PET", 2L),
                    createProduct("可伸缩宠物牵引绳 5m", 6L, new BigDecimal("39.90"), 80,
                            "一键伸缩锁定，5米长度自由活动。", "/uploads/products/pet-leash.jpg", "ACCESSORY", 2L),
                    createProduct("不锈钢宠物食碗 防滑", 6L, new BigDecimal("18.90"), 160,
                            "304不锈钢材质，防锈耐摔，底部防滑硅胶圈。", "/uploads/products/pet-bowl-steel.jpg", "ACCESSORY", 2L),
                    createProduct("宠物营养膏 120g", 7L, new BigDecimal("38.00"), 130,
                            "高浓缩营养补充，含多种维生素矿物质。", "/uploads/products/pet-supplement-paste.jpg", "ACCESSORY", 1L),
                    createProduct("宠物益生菌粉 5g×30包", 7L, new BigDecimal("68.00"), 75,
                            "进口益生菌菌株，调理肠胃，增强免疫力。", "/uploads/products/pet-supplement-probiotics.jpg", "ACCESSORY", 1L),
                    createProduct("卵磷脂美毛粉 500g", 7L, new BigDecimal("55.00"), 90,
                            "天然大豆卵磷脂，促进毛发生长，减少掉毛。", "/uploads/products/pet-supplement-lecithin.jpg", "ACCESSORY", 1L),
                    createProduct("太空舱宠物双肩背包", 8L, new BigDecimal("129.00"), 35,
                            "透明太空舱设计，透气通风，双肩减压背负。", "/uploads/products/pet-carrier-backpack.jpg", "ACCESSORY", 2L),
                    createProduct("便携航空箱 宠物旅行箱", 8L, new BigDecimal("159.00"), 25,
                            "航空标准尺寸，结实耐用，适合飞行托运。", "/uploads/products/pet-carrier-cage.jpg", "ACCESSORY", 2L),
                    // PET类型（活体宠物）- 单品唯一
                    createProduct("英短蓝猫 3个月大 纯种", 1L, new BigDecimal("2800.00"), 1,
                            "纯种英短蓝猫，已打疫苗，健康活泼，适合家庭饲养。", "/uploads/products/cat-food-fish.jpg", "PET", 1L),
                    createProduct("金毛幼犬 2个月 赛级血统", 2L, new BigDecimal("3500.00"), 1,
                            "赛级血统金毛幼犬，性格温顺，聪明易训练。", "/uploads/products/dog-food-puppy.jpg", "PET", 1L)
            );
            productRepository.saveAll(products);
            log.info("初始化商品: {} 个", products.size());
        }
    }

    private void initStores() {
        if (storeRepository.count() == 0) {
            List<Store> stores = List.of(
                    createStore("萌宠优品旗舰店(朝阳店)",
                            "北京市朝阳区建国路88号SOHO现代城A座1层",
                            "010-88880001",
                            BigDecimal.valueOf(39.9087), BigDecimal.valueOf(116.4715),
                            "萌宠优选旗舰店，商品品类最全的线下体验店，支持到店自提和宠物寄养服务。",
                            "09:00-21:00"),
                    createStore("萌宠优选(海淀店)",
                            "北京市海淀区中关村大街15号中关村广场B1",
                            "010-88880002",
                            BigDecimal.valueOf(39.9836), BigDecimal.valueOf(116.3059),
                            "位于中关村核心地带，主要服务周边居民和学生群体，提供宠物用品和洗护服务。",
                            "10:00-20:00"),
                    createStore("萌宠优选(望京店)",
                            "北京市朝阳区望京街9号望京国际商业中心2层",
                            "010-88880003",
                            BigDecimal.valueOf(39.9980), BigDecimal.valueOf(116.4825),
                            "望京区域旗舰店，提供宠物用品、美容护理、宠物医疗一站式服务。",
                            "09:30-21:30"),
                    createStore("萌宠优选(通州店)",
                            "北京市通州区新华西街58号万达广场B1",
                            "010-88880004",
                            BigDecimal.valueOf(39.9053), BigDecimal.valueOf(116.6577),
                            "通州区域首家萌宠优选门店，提供丰富的宠物食品和玩具选择。",
                            "10:00-22:00"),
                    createStore("萌宠优选(昌平店)",
                            "北京市昌平区回龙观东大街1号龙域中心1层",
                            "010-88880005",
                            BigDecimal.valueOf(40.0781), BigDecimal.valueOf(116.3356),
                            "回龙观地区门店，提供宠物食品、用品和洗护美容服务。",
                            "09:00-20:00")
            );
            storeRepository.saveAll(stores);
            log.info("初始化商店: {} 个", stores.size());
        }
    }

    private void initVideos() {
        if (videoRepository.count() == 0) {
            List<Video> videos = List.of(
                    createVideo("猫咪正确喂养指南", "详细讲解猫咪日常喂养的注意事项和科学方法。", "PET_FOOD", 1L),
                    createVideo("狗狗基础训练教程", "教你在家训练狗狗基本指令，建立良好行为习惯。", "PET_PLAY", 2L),
                    createVideo("如何选择适合的猫粮", "不同年龄段猫咪的猫粮选择指南，帮你避开选购误区。", "PET_FOOD", 1L),
                    createVideo("给狗狗洗澡的技巧", "在家给狗狗洗澡的正确步骤和注意事项。", "PET_CARE", 2L),
                    createVideo("皇家猫粮深度测评", "皇家室内成猫粮全面测评，分析成分和适口性。", "PRODUCT_INTRO", 1L),
                    createVideo("冠能犬粮开箱评测", "冠能中型犬成犬粮实际体验，狗狗爱吃吗？", "PRODUCT_INTRO", 2L),
                    createVideo("猫咪玩具推荐TOP5", "评选最受猫咪欢迎的5款玩具，主人必看。", "PRODUCT_INTRO", 1L),
                    createVideo("萌宠日常搞笑合集", "铲屎官和毛孩子的欢乐日常，治愈你的不开心。", "PET_PLAY", null)
            );
            videoRepository.saveAll(videos);
            log.info("初始化视频: {} 个", videos.size());
        }
    }

    private Store createStore(String name, String address, String phone, BigDecimal lat, BigDecimal lng, String desc, String bizHours) {
        Store s = new Store();
        s.setName(name);
        s.setAddress(address);
        s.setPhone(phone);
        s.setLatitude(lat);
        s.setLongitude(lng);
        s.setDescription(desc);
        s.setBusinessHours(bizHours);
        s.setStatus(1);
        return s;
    }

    private Video createVideo(String title, String description, String category, Long productId) {
        Video v = new Video();
        v.setTitle(title);
        v.setDescription(description);
        v.setCategory(category);
        v.setProductId(productId);
        v.setVideoUrl("");  // 演示数据暂无实际视频URL
        v.setCoverUrl("");
        v.setViewCount(0L);
        return v;
    }

    private Category createCategory(String name, String icon) {
        Category c = new Category();
        c.setName(name);
        c.setIcon(icon);
        return c;
    }

    private Product createProduct(String name, Long categoryId, BigDecimal price, int stock, String desc, String image, String productType, Long storeId) {
        Product p = new Product();
        p.setName(name);
        p.setCategoryId(categoryId);
        p.setPrice(price);
        p.setStock(stock);
        p.setDescription(desc);
        p.setImage(image);
        p.setProductType(productType);
        p.setStoreId(storeId);
        p.setStatus(1);
        return p;
    }
}
