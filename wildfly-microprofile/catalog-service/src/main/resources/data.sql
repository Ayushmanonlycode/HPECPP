-- Categories
INSERT INTO categories (id, name, description) VALUES
('FISH', 'Fish', 'Various fish species'),
('DOGS', 'Dogs', 'Various dog breeds'),
('REPTILES', 'Reptiles', 'Various reptile species'),
('CATS', 'Cats', 'Various cat breeds'),
('BIRDS', 'Birds', 'Various bird species')
ON CONFLICT (id) DO NOTHING;

-- Products
INSERT INTO products (id, name, description, species, category_id) VALUES
('FI-SW-01', 'Angelfish', 'Saltwater Angelfish', 'Fish', 'FISH'),
('FI-SW-02', 'Tiger Shark', 'Saltwater Tiger Shark', 'Fish', 'FISH'),
('FI-FW-01', 'Koi', 'Freshwater Koi', 'Fish', 'FISH'),
('FI-FW-02', 'Goldfish', 'Freshwater Goldfish', 'Fish', 'FISH'),
('K9-BD-01', 'Bulldog', 'English Bulldog', 'Dog', 'DOGS'),
('K9-PO-02', 'Poodle', 'Standard Poodle', 'Dog', 'DOGS'),
('K9-DL-01', 'Dalmatian', 'Dalmatian', 'Dog', 'DOGS'),
('K9-RT-01', 'Golden Retriever', 'Golden Retriever', 'Dog', 'DOGS'),
('K9-RT-02', 'Labrador Retriever', 'Labrador Retriever', 'Dog', 'DOGS'),
('K9-CW-01', 'Chihuahua', 'Chihuahua', 'Dog', 'DOGS'),
('RP-SN-01', 'Rattlesnake', 'Rattlesnake', 'Reptile', 'REPTILES'),
('RP-LI-02', 'Iguana', 'Iguana', 'Reptile', 'REPTILES'),
('FL-DSH-01', 'Manx', 'Manx Cat', 'Cat', 'CATS'),
('FL-DSH-02', 'Persian', 'Persian Cat', 'Cat', 'CATS'),
('AV-SB-02', 'Finch', 'Finch', 'Bird', 'BIRDS'),
('AV-CB-01', 'Amazon Parrot', 'Amazon Parrot', 'Bird', 'BIRDS'),
('AV-CB-02', 'Amazon Macaw', 'Amazon Macaw', 'Bird', 'BIRDS')
ON CONFLICT (id) DO NOTHING;

-- Items
INSERT INTO items (id, sku, list_price, description, image_url, product_id) VALUES
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
