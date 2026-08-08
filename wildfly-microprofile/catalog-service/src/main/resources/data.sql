-- Categories
INSERT INTO category (id, categoryName) VALUES
('FISH', 'Fish'),
('DOGS', 'Dogs'),
('REPTILES', 'Reptiles'),
('CATS', 'Cats'),
('BIRDS', 'Birds')
ON CONFLICT (id) DO NOTHING;

-- Products
INSERT INTO product (id, name, availability, category_id, categoryName) VALUES
('FI-SW-01', 'Angelfish', 'In Stock', 'FISH', 'Fish'),
('FI-SW-02', 'Tiger Shark', 'In Stock', 'FISH', 'Fish'),
('FI-FW-01', 'Koi', 'In Stock', 'FISH', 'Fish'),
('FI-FW-02', 'Goldfish', 'In Stock', 'FISH', 'Fish'),
('K9-BD-01', 'Bulldog', 'In Stock', 'DOGS', 'Dogs'),
('K9-PO-02', 'Poodle', 'In Stock', 'DOGS', 'Dogs'),
('K9-DL-01', 'Dalmatian', 'In Stock', 'DOGS', 'Dogs'),
('K9-RT-01', 'Golden Retriever', 'In Stock', 'DOGS', 'Dogs'),
('K9-RT-02', 'Labrador Retriever', 'In Stock', 'DOGS', 'Dogs'),
('K9-CW-01', 'Chihuahua', 'In Stock', 'DOGS', 'Dogs'),
('RP-SN-01', 'Rattlesnake', 'In Stock', 'REPTILES', 'Reptiles'),
('RP-LI-02', 'Iguana', 'In Stock', 'REPTILES', 'Reptiles'),
('FL-DSH-01', 'Manx', 'In Stock', 'CATS', 'Cats'),
('FL-DSH-02', 'Persian', 'In Stock', 'CATS', 'Cats'),
('AV-SB-02', 'Finch', 'In Stock', 'BIRDS', 'Birds'),
('AV-CB-01', 'Amazon Parrot', 'In Stock', 'BIRDS', 'Birds'),
('AV-CB-02', 'Amazon Macaw', 'In Stock', 'BIRDS', 'Birds')
ON CONFLICT (id) DO NOTHING;

-- Items
INSERT INTO item (id, sku, listPrice, itemName, imageUrl, product_id) VALUES
('EST-1', 'EST-1', 16.50, 'Large Angelfish', '/images/EST-1.png', 'FI-SW-01'),
('EST-2', 'EST-2', 16.50, 'Small Angelfish', '/images/EST-2.png', 'FI-SW-01'),
('EST-3', 'EST-3', 18.50, 'Toothy Tiger Shark', '/images/EST-3.png', 'FI-SW-02'),
('EST-4', 'EST-4', 18.50, 'Spotted Koi', '/images/EST-4.png', 'FI-FW-01'),
('EST-5', 'EST-5', 18.50, 'Spotless Koi', '/images/EST-5.png', 'FI-FW-01'),
('EST-6', 'EST-6', 18.50, 'Male Adult Bulldog', '/images/EST-6.png', 'K9-BD-01'),
('EST-7', 'EST-7', 18.50, 'Female Puppy Bulldog', '/images/EST-7.png', 'K9-BD-01'),
('EST-8', 'EST-8', 18.50, 'Male Puppy Poodle', '/images/EST-8.png', 'K9-PO-02'),
('EST-9', 'EST-9', 18.50, 'Spotless Male Puppy Dalmatian', '/images/EST-9.png', 'K9-DL-01'),
('EST-10', 'EST-10', 18.50, 'Spotted Female Puppy Dalmatian', '/images/EST-10.png', 'K9-DL-01'),
('EST-11', 'EST-11', 18.50, 'Venomless Rattlesnake', '/images/EST-11.png', 'RP-SN-01'),
('EST-12', 'EST-12', 18.50, 'Rattleless Rattlesnake', '/images/EST-12.png', 'RP-SN-01'),
('EST-13', 'EST-13', 18.50, 'Green Adult Iguana', '/images/EST-13.png', 'RP-LI-02'),
('EST-14', 'EST-14', 58.50, 'Tailless Manx', '/images/EST-14.png', 'FL-DSH-01'),
('EST-15', 'EST-15', 23.50, 'Adult Female Persian', '/images/EST-15.png', 'FL-DSH-02'),
('EST-16', 'EST-16', 93.50, 'Adult Male Persian', '/images/EST-16.png', 'FL-DSH-02'),
('EST-17', 'EST-17', 93.50, 'Adult Male Finch', '/images/EST-17.png', 'AV-SB-02'),
('EST-18', 'EST-18', 193.50, 'Adult Male Amazon Parrot', '/images/EST-18.png', 'AV-CB-01'),
('EST-19', 'EST-19', 15.50, 'Adult Female Amazon Macaw', '/images/EST-19.png', 'AV-CB-02'),
('EST-20', 'EST-20', 5.50, 'Adult Female Goldfish', '/images/EST-20.png', 'FI-FW-02'),
('EST-21', 'EST-21', 5.29, 'Adult Male Goldfish', '/images/EST-21.png', 'FI-FW-02'),
('EST-22', 'EST-22', 135.50, 'Adult Male Labrador Retriever', '/images/EST-22.png', 'K9-RT-02'),
('EST-23', 'EST-23', 145.49, 'Adult Female Labrador Retriever', '/images/EST-23.png', 'K9-RT-02'),
('EST-24', 'EST-24', 125.50, 'Adult Male Chihuahua', '/images/EST-24.png', 'K9-CW-01'),
('EST-25', 'EST-25', 155.29, 'Adult Female Chihuahua', '/images/EST-25.png', 'K9-CW-01'),
('EST-26', 'EST-26', 155.29, 'Adult Female Golden Retriever', '/images/EST-26.png', 'K9-RT-01')
ON CONFLICT (id) DO NOTHING;
